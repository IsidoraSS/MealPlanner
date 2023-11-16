package rs.raf.mealPlanner.presentation.view.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.mealPlanner.R
import rs.raf.mealPlanner.data.models.utils.DateCount
import rs.raf.mealPlanner.data.models.utils.MealDetails
import rs.raf.mealPlanner.databinding.FragmentMiscBinding
import rs.raf.mealPlanner.presentation.contract.MainContract
import rs.raf.mealPlanner.presentation.view.states.DateCountState
import rs.raf.mealPlanner.presentation.view.states.MealIdsState
import rs.raf.mealPlanner.presentation.view.states.StatisticsMealsState
import rs.raf.mealPlanner.presentation.viewmodel.MainViewModel
import timber.log.Timber


class MiscFragment : Fragment(R.layout.fragment_misc) {

    private val mainViewModel: MainContract.ViewModel by sharedViewModel<MainViewModel>()

    private var _binding: FragmentMiscBinding? = null

    private val binding get() = _binding!!

    lateinit var barChart: BarChart
    lateinit var barData: BarData
    lateinit var barDataSet: BarDataSet
    lateinit var barEntry: ArrayList<BarEntry>

    private var shouldGet = true

    private var mealList= mutableListOf<MealDetails>()

    private var percentageMeals=mutableMapOf<String?, Float>()
    private var percentageCategories=mutableMapOf<String?, Float>()
    private var percentageArea=mutableMapOf<String?, Float>()

    lateinit var pieChart: PieChart
    lateinit var pieData: PieData
    lateinit var pieDataSet: PieDataSet
    lateinit var pieEntry: ArrayList<PieEntry>

    private var x=3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMiscBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun initObservers() {
        mainViewModel.weeklyCount.observe(viewLifecycleOwner, Observer {
            renderState(it)
        })
        mainViewModel.getWeeklyCount()

        mainViewModel.statisticsMealDetails.observe(viewLifecycleOwner,Observer{
            createLists(it)
            pieChart.notifyDataSetChanged()
            pieChart.invalidate()
        })

        mainViewModel.mealIdsState.observe(viewLifecycleOwner, Observer {
            when(it){
                is MealIdsState.Success -> {
//                    if(shouldGet){
                    Timber.e("AJDIJEVI" + it.mealIds.toString())
                        mainViewModel.getStatisticMealsDetails(it.mealIds)
//                        shouldGet=false
//                    }
                }
            }
        })
        mainViewModel.getMealIdsState()
    }

    private fun init(){
        initObservers()
        initTypeDropdown()
        initCountDropdown()
        initListeners()
    }

    private fun initListeners(){
        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                val selectedItem = parent!!.getItemAtPosition(position).toString()
                if(selectedItem=="Areas"){
                    Timber.e("Meal")
                    getPieEntriesArea()
                }
                else if(selectedItem=="Categories"){
                    Timber.e("Cat")
                    getPieEntriesCategory()
                }
                else if(selectedItem=="Meals"){
                    Timber.e("ING")
                    getPieEntriesMeal()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?){

            }
        }

        binding.spinnerCount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                val selectedItem = parent!!.getItemAtPosition(position).toString()
                if(selectedItem=="1"){
                    x=1
                }
                else if(selectedItem=="3"){
                    x=3
                }
                else if(selectedItem=="5"){
                    x=5
                }
                else if(selectedItem=="10"){
                    x=10
                }

                if(binding.spinnerType.selectedItem=="Areas"){
                    getPieEntriesArea()
                }
                else if(binding.spinnerType.selectedItem=="Categories"){
                    getPieEntriesCategory()
                }
                else{
                    getPieEntriesMeal()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?){

            }
        }
    }


    private fun initTypeDropdown(){
        val dropdown=binding.spinnerType
        dropdown.bringToFront()
        val staticAdapter = ArrayAdapter
            .createFromResource(
                this.context!!, R.array.typeDD,
                android.R.layout.simple_dropdown_item_1line
            )
        dropdown.adapter=staticAdapter
    }

    private fun initCountDropdown(){
        val dropdown=binding.spinnerCount
        dropdown.bringToFront()
        val staticAdapter = ArrayAdapter
            .createFromResource(
                this.context!!, R.array.countDD,
                android.R.layout.simple_dropdown_item_1line
            )
        dropdown.adapter=staticAdapter
    }

    private fun getBarEntries(weeklyCount:List<DateCount>){
        barEntry = ArrayList()
        for(item in weeklyCount){
            barEntry.add(BarEntry(item.dateNumber.toFloat(),item.count.toFloat()))
        }
        barChart=binding.barChart
        barDataSet= BarDataSet(barEntry,"Number of meals added in the last week")
        barData=BarData(barDataSet)
        barChart.data=barData
        barDataSet.color=resources.getColor(R.color.md_theme_light_primary)
        barDataSet.valueTextColor= Color.BLACK
        barDataSet.valueTextSize=16f
        barChart.description.isEnabled=false
        barChart.notifyDataSetChanged()
        barChart.invalidate()
    }

    private fun getPieEntriesMeal(){
        pieEntry = ArrayList()
        var i = 0
        var other=100f
        for(item in percentageMeals){
            if(i==x) break
            pieEntry.add(PieEntry(item.value,item.key))
            other=other-item.value
            i++
        }
        pieEntry.add(PieEntry(other,"Other"))
        pieChart=binding.pieChart
        pieDataSet= PieDataSet(pieEntry,"Meals")
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS,255)
        pieDataSet.valueTextSize=16f
        pieDataSet.valueTextColor=Color.BLACK

        pieData=PieData(pieDataSet)
        pieChart.data=pieData
        Timber.e(pieChart.data.toString())
        pieChart.description.text="${x} favorite Meals"
        pieChart.notifyDataSetChanged()
        pieChart.invalidate()
    }

    private fun getPieEntriesCategory(){
        pieEntry = ArrayList()
        var i = 0
        var other=100f
        for(item in percentageCategories){
            if(i==x) break
            pieEntry.add(PieEntry(item.value,item.key))
            other=other-item.value
            i++
        }
        pieEntry.add(PieEntry(other,"Other"))
        pieChart=binding.pieChart
        pieDataSet= PieDataSet(pieEntry,"Categories")
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS,255)
        pieDataSet.valueTextSize=16f
        pieDataSet.valueTextColor=Color.BLACK

        pieData=PieData(pieDataSet)
        pieChart.data=pieData

        pieChart.description.text="${x} favorite Categories"
        pieChart.notifyDataSetChanged()
        pieChart.invalidate()
    }

    private fun getPieEntriesArea(){
        pieEntry = ArrayList()
        var i = 0
        var other=100f
        for(item in percentageArea){
            if(i==x) break
            pieEntry.add(PieEntry(item.value,item.key))
            other=other-item.value
            i++
        }
        pieEntry.add(PieEntry(other,"Other"))
        pieChart=binding.pieChart
        pieDataSet= PieDataSet(pieEntry,"Areas")
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS,255)
        pieDataSet.valueTextSize=16f
        pieDataSet.valueTextColor=Color.BLACK

        pieData=PieData(pieDataSet)
        pieChart.data=pieData

        pieChart.description.text="${x} favorite Areas"
        pieChart.notifyDataSetChanged()
        pieChart.invalidate()
    }

    private fun renderState(state: DateCountState) {
        when(state) {
            is DateCountState.Success -> {
                getBarEntries(state.weeklyCount)
            }
            is DateCountState.Error -> Toast.makeText(context, "Error happened", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun createLists(state: StatisticsMealsState){
        when(state) {
            is StatisticsMealsState.Success -> {
                //ovde pravim liste jela, kategorija i predela
                mealList.clear()
                mealList.addAll(state.fullMeals)
                createMealMap()
                createCategoryMap()
                createAreaMap()
                if(binding.spinnerType.selectedItem=="Areas"){
                    getPieEntriesArea()
                }
                else if(binding.spinnerType.selectedItem=="Categories"){
                    getPieEntriesCategory()
                }
                else{
                    getPieEntriesMeal()
                }
            }
            is StatisticsMealsState.Error -> Toast.makeText(context, "Error happened", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun createMealMap(){
        val nameOccurrenceMap = mealList
            .filter { it.name != null } // Filter out items with null names
            .groupBy { it.name } // Group by name
            .mapValues { (_, value) -> value.size }

        val sorted = nameOccurrenceMap.toList().sortedBy { (_, value) -> value}.reversed().toMap()
        percentageMeals.putAll(sorted.mapValues { (_, value) -> (value.toFloat() / mealList.size) * 100 })
        Timber.e(percentageMeals.toString())
    }

    private fun createCategoryMap(){
        val categoryOccurrenceMap = mealList
            .filter { it.category != null }
            .groupBy { it.category }
            .mapValues { (_, value) -> value.size }

        val sorted = categoryOccurrenceMap.toList().sortedBy { (_, value) -> value}.reversed().toMap()
        percentageCategories.putAll(sorted.mapValues { (_, value) -> (value.toFloat() / mealList.size) * 100 })
        Timber.e(percentageCategories.toString())
    }

    private fun createAreaMap(){
        val areaOccurrenceMap = mealList
            .filter { it.area != null }
            .groupBy { it.area }
            .mapValues { (_, value) -> value.size }

        val sorted = areaOccurrenceMap.toList().sortedBy { (_, value) -> value}.reversed().toMap()
        percentageArea.putAll(sorted.mapValues { (_, value) -> (value.toFloat() / mealList.size) * 100 })
        Timber.e(percentageArea.toString())
    }

}