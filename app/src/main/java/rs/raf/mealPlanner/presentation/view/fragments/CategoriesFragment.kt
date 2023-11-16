package rs.raf.mealPlanner.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.mealPlanner.R
import rs.raf.mealPlanner.data.models.utils.Category
import rs.raf.mealPlanner.databinding.FragmentCategoriesBinding
import rs.raf.mealPlanner.presentation.contract.MainContract
import rs.raf.mealPlanner.presentation.view.activities.MainActivity
import rs.raf.mealPlanner.presentation.view.recycler.adapter.CategoryAdapter
import rs.raf.mealPlanner.presentation.view.states.CategoriesState
import rs.raf.mealPlanner.presentation.viewmodel.MainViewModel
import timber.log.Timber

class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    private val mainViewModel: MainContract.ViewModel by sharedViewModel<MainViewModel>()

    private var _binding: FragmentCategoriesBinding? = null


    private val binding get() = _binding!!

    private lateinit var adapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initUi()
        initObservers()
    }

    private fun initUi() {
        initRecycler()
        initListeners()
    }

    private fun initRecycler() {
        binding.listRv.layoutManager = GridLayoutManager(context,3)
        adapter = CategoryAdapter(this)
        binding.listRv.adapter = adapter
    }

    private fun initListeners() {
//        binding.inputEt.doAfterTextChanged {
//            val filter = it.toString()
//            mainViewModel.getMealsByName(filter)
//        }
    }

    private fun initObservers() {
        mainViewModel.categoriesState.observe(viewLifecycleOwner, Observer {
            Timber.e(it.toString())
            renderState(it)
        })

        mainViewModel.fetchCategories()
    }

    private fun renderState(state: CategoriesState) {
        when (state) {
            is CategoriesState.Success -> {
                showLoadingState(false)
                adapter.submitList(state.categories)
            }
            is CategoriesState.Error -> {
                showLoadingState(false)
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is CategoriesState.DataFetched -> {
                showLoadingState(false)
                Toast.makeText(context, "Fresh data fetched from the server", Toast.LENGTH_LONG).show()
            }
            is CategoriesState.Loading -> {
                showLoadingState(true)
            }
        }
    }

    private fun showLoadingState(loading: Boolean) {
        //binding.inputEt.isVisible = !loading
        binding.listRv.isVisible = !loading
        binding.loadingPb.isVisible = loading
    }

    fun selectCategoryInMainViewModel(category: Category){ //setuje selektovanu kategoriju u view modelu, to se zove iz CategoryViewHoldera na klik na item
        mainViewModel.setSelectedCategory(category)
    }

    fun slideToFilterTab(){
        val myActivity = activity as MainActivity
        myActivity.slideToFilterTab()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}