package rs.raf.mealPlanner.presentation.view.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.mealPlanner.R
import rs.raf.mealPlanner.data.models.utils.Filter
import rs.raf.mealPlanner.data.models.utils.Meal
import rs.raf.mealPlanner.databinding.FilterDropdownDialogBinding
import rs.raf.mealPlanner.databinding.FragmentFilterBinding
import rs.raf.mealPlanner.presentation.contract.MainContract
import rs.raf.mealPlanner.presentation.view.activities.MainActivity
import rs.raf.mealPlanner.presentation.view.recycler.adapter.FilterAdapter
import rs.raf.mealPlanner.presentation.view.recycler.adapter.MealAdapter
import rs.raf.mealPlanner.presentation.view.states.FiltersState
import rs.raf.mealPlanner.presentation.view.states.MealsState
import rs.raf.mealPlanner.presentation.viewmodel.MainViewModel



class FilterFragment : Fragment(R.layout.fragment_filter) {

    // Koristimo by sharedViewModel jer sada view modele instanciramo kroz koin
    private val mainViewModel: MainContract.ViewModel by sharedViewModel<MainViewModel>()

    private var _binding: FragmentFilterBinding? = null

    private val binding get() = _binding!!

    private lateinit var dialog:Dialog
    private lateinit var dropdownAdapter: FilterAdapter
    private lateinit var dialogBinding: FilterDropdownDialogBinding

    private lateinit var adapter: MealAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    companion object {
        const val CATEGORY = 0
        const val AREA = 1
        const val INGREDIENT = 2
    }


    private fun init() {
        initObservers()
        initListeners()
        initStaticDropdown()
        initDropdownDialog()
        initRecycler()
    }

    private fun initObservers() {
        mainViewModel.selectedCategory.observe(viewLifecycleOwner, Observer {
            selectCategoryInStaticSpinner()
            setDynamicSpinnerValue(mainViewModel.selectedCategory.value?.name ?: "")
        })

        mainViewModel.filtersState.observe(viewLifecycleOwner, Observer {
            renderDialogState(it)
        })

        mainViewModel.selectedFilter.observe(viewLifecycleOwner, Observer{
            setDynamicSpinnerValue(it.name)
        })

        mainViewModel.mealsState.observe(viewLifecycleOwner,Observer{
            renderState(it)
        })
    }

    private fun initListeners(){
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                val selectedItem = parent!!.getItemAtPosition(position).toString()
                if(selectedItem=="Area"){
                    mainViewModel.fetchAreaNames()
                }
                else if(selectedItem=="Category"){
                    mainViewModel.fetchCategoryNames()
                }
                else if(selectedItem=="Ingredient"){
                    mainViewModel.fetchIngredientNames()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?){

            }
        }

        binding.dynamicSpinner.setOnClickListener{
            dialog.show()
        }

        binding.dynamicSpinner.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var selection:String = s.toString()
                var spinnerSelectedItem=binding.spinner.selectedItem.toString()

                if(spinnerSelectedItem=="Category"){
                    mainViewModel.fetchMealsByCategory(selection)
                }
                else if(spinnerSelectedItem=="Area"){
                    mainViewModel.fetchMealsByArea(selection)
                }
                else{
                    mainViewModel.fetchMealsByIngredient(selection)
                }
            }
        })

    }

    private fun initRecycler() {
        binding.listRv.layoutManager = LinearLayoutManager(this.context)
        adapter = MealAdapter(this)
        binding.listRv.adapter = adapter
    }

    private fun initStaticDropdown(){
        val dropdown=binding.spinner
        val staticAdapter = ArrayAdapter
            .createFromResource(
                this.context!!, R.array.filterDD,
                android.R.layout.simple_dropdown_item_1line
            )
        dropdown.adapter=staticAdapter
    }

    private fun initDropdownDialog(){
        dialog=Dialog(this.requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)

        dialogBinding = FilterDropdownDialogBinding.inflate(LayoutInflater.from(this.context))
        dialog.setContentView(dialogBinding.getRoot())

        val width = (this.getResources().getDisplayMetrics().widthPixels * 0.9)
        dialog.window!!.setLayout(width.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.search.clearFocus()
        dialogBinding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                dropdownAdapter.filter.filter(newText)
                return true
            }
        })

        dialogBinding.listRv.layoutManager=LinearLayoutManager(this.context)
        dropdownAdapter = FilterAdapter(this)
        dialogBinding.listRv.adapter=dropdownAdapter
    }

    private fun renderDialogState(state: FiltersState) {
        when (state) {
            is FiltersState.Success -> {
                showDialogLoadingState(false)
                dropdownAdapter.setData(state.filters)
                val dropdown=binding.dynamicSpinner
                dropdown.setText(state.filters.get(0).name)
            }
            is FiltersState.Error -> {
                showDialogLoadingState(false)
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is FiltersState.DataFetched -> {
                showDialogLoadingState(false)
                Toast.makeText(context, "Fresh data fetched from the server", Toast.LENGTH_LONG).show()
            }
            is FiltersState.Loading -> {
                showDialogLoadingState(true)
            }
        }
    }

    private fun showDialogLoadingState(loading: Boolean) {
        dialogBinding.search.isVisible = !loading
        dialogBinding.listRv.isVisible = !loading
        dialogBinding.loadingPb.isVisible = loading
    }

    private fun renderState(state: MealsState) {
        when (state) {
            is MealsState.Success -> {
                showLoadingState(false)
                adapter.submitList(state.meals)
            }
            is MealsState.Error -> {
                showLoadingState(false)
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is MealsState.DataFetched -> {
                showLoadingState(false)
                Toast.makeText(context, "Fresh data fetched from the server", Toast.LENGTH_LONG).show()
            }
            is MealsState.Loading -> {
                showLoadingState(true)
            }
        }
    }

    private fun showLoadingState(loading: Boolean) {
        //binding.inputEt.isVisible = !loading
        binding.listRv.isVisible = !loading
        binding.loadingPb.isVisible = loading
    }

    fun selectCategoryInStaticSpinner(){
        val dropdown=binding.spinner
        dropdown.setSelection(CATEGORY)
    }

    fun setDynamicSpinnerValue(value: String) {
        val dropdown=binding.dynamicSpinner
        dropdown.setText(value)
    }

    fun selectFilterInMainViewModel(filter: Filter){
        mainViewModel.setSelectedFilter(filter)
    }

    fun selectMealInMainViewModel(meal: Meal) {
        mainViewModel.setSelectedMeal(meal)
        mainViewModel.fetchMealDetails(meal)
    }

    fun slideToMealTab() {
        val myActivity = activity as MainActivity
        myActivity.slideToMealTab()
    }

    fun dismissDialog(){
        dialogBinding = FilterDropdownDialogBinding.inflate(LayoutInflater.from(this.context))
        dialogBinding.search.setQuery("",true)
        dialogBinding.search.clearFocus()
        dialog.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}