package rs.raf.mealPlanner.presentation.view.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.mealPlanner.R
import rs.raf.mealPlanner.data.models.entities.MealEntity
import rs.raf.mealPlanner.databinding.FragmentEditMealBinding
import rs.raf.mealPlanner.presentation.contract.MainContract
import rs.raf.mealPlanner.presentation.view.states.MealDetailsState
import rs.raf.mealPlanner.presentation.view.states.SaveMealState
import rs.raf.mealPlanner.presentation.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*


class EditMealFragment : Fragment(R.layout.fragment_edit_meal) {

    private val mainViewModel: MainContract.ViewModel by sharedViewModel<MainViewModel>()

    private var _binding: FragmentEditMealBinding? = null

    private val binding get() = _binding!!

    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        initObservers()
        initListeners()
        initStaticDropdown()
        initDatePicker()
        initDateButtonText()
    }

    private fun initObservers(){
        mainViewModel.selectedMealDetails.observe(viewLifecycleOwner, Observer{
            populateView(it)
        })

        mainViewModel.mealSaved.observe(viewLifecycleOwner,Observer{
            renderState(it)
        })
    }

    private fun initListeners(){
        binding.datePickerButton.setOnClickListener{
            showDatePicker()
        }
        binding.saveButton.setOnClickListener{
            saveMealToDB(mainViewModel.selectedMealDetails.value!!)
        }
        binding.floatingActionButton2.setOnClickListener{
            val parent=parentFragment as MealContainerFragment
            parent.replaceEditWithDetails()
        }
    }

    private fun populateView(state: MealDetailsState){
        when (state) {
            is MealDetailsState.Success -> {
                Glide.with(this)
                    .load(state.mealDetails.image)
                    .fitCenter()
                    .circleCrop()
                    .into(binding.imageView2)
                binding.title.setText(state.mealDetails.name)
            }
        }
        //ako ne radi onda umesto direktno preko selected meala, na save dugme u MealFragmentu da se u MainViewModel
        //doda mealToEdit koji je samo obican MealDetails a ne State
    }

    private fun initStaticDropdown(){
        val dropdown=binding.spinner
        val staticAdapter = ArrayAdapter
            .createFromResource(
                this.context!!, R.array.mealTypeDD,
                android.R.layout.simple_dropdown_item_1line
            )
        dropdown.adapter=staticAdapter
    }

    fun initDatePicker(){
        val dateSetListener =
            OnDateSetListener { datePicker, year, month, day ->
                var month = month
                month = month + 1
                val date: String = makeDateString(day, month, year)
                binding.datePickerButton.setText(date)
            }

        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        val month: Int = cal.get(Calendar.MONTH)
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)

        val style: Int = AlertDialog.THEME_HOLO_LIGHT

        datePickerDialog = DatePickerDialog(context!!, style, dateSetListener, year, month, day)
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return getMonthFormat(month) + " " + day + " " + year
    }

    private fun getMonthFormat(month: Int): String? {
        if (month == 1) return "JAN"
        if (month == 2) return "FEB"
        if (month == 3) return "MAR"
        if (month == 4) return "APR"
        if (month == 5) return "MAY"
        if (month == 6) return "JUN"
        if (month == 7) return "JUL"
        if (month == 8) return "AUG"
        if (month == 9) return "SEP"
        if (month == 10) return "OCT"
        if (month == 11) return "NOV"
        return if (month == 12) "DEC" else "JAN"
    }

    private fun initDateButtonText(){
        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        val month: Int = cal.get(Calendar.MONTH)
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        val date: String = makeDateString(day, month+1, year)
        binding.datePickerButton.text=date
    }

    private fun renderState(state: SaveMealState) {
        when(state) {
            is SaveMealState.Success -> Toast.makeText(context, "Meal saved", Toast.LENGTH_SHORT)
                .show()
            is SaveMealState.Error -> Toast.makeText(context, "Error happened", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showDatePicker() {
        datePickerDialog.show()
    }

    private fun saveMealToDB(state: MealDetailsState){
        when (state) {
            is MealDetailsState.Success -> {
                val meal = state.mealDetails
                val name = binding.title.text.takeIf { it.isNotBlank() } ?: meal.name
                val mealEntity=MealEntity(meal.id,name.toString(),meal.category,meal.area,meal.recipe,meal.image,
                    meal.youtube,meal.tags,meal.ingredients,convertStringToDate(binding.datePickerButton.text.toString()),binding.spinner.selectedItem.toString())
                mainViewModel.saveMeal(mealEntity)
                val parent=parentFragment as MealContainerFragment
                parent.replaceEditWithDetails()
            }
        }
    }

    fun convertStringToDate(dateString: String): Date? {
        val pattern = "MMM dd yyyy"
        val sdf = SimpleDateFormat(pattern, Locale.US)

        return try {
            sdf.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

}