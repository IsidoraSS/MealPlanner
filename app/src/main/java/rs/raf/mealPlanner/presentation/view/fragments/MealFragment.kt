package rs.raf.mealPlanner.presentation.view.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.mealPlanner.R
import rs.raf.mealPlanner.data.models.utils.MealDetails
import rs.raf.mealPlanner.databinding.FragmentMealDetailsBinding
import rs.raf.mealPlanner.presentation.contract.MainContract
import rs.raf.mealPlanner.presentation.view.states.MealDetailsState
import rs.raf.mealPlanner.presentation.viewmodel.MainViewModel
import java.util.regex.Matcher
import java.util.regex.Pattern


class MealFragment : Fragment(R.layout.fragment_meal_details) {

    private val mainViewModel: MainContract.ViewModel by sharedViewModel<MainViewModel>()

    private var _binding: FragmentMealDetailsBinding? = null

    private val binding get() = _binding!!
    private var ytLink=""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMealDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        initObservers()
        initListeners()
    }

    private fun initObservers(){
        mainViewModel.selectedMealDetails.observe(viewLifecycleOwner, Observer{
            renderState(it)
        })
    }

    private fun initListeners(){
        binding.floatingActionButton2.setOnClickListener{
            val parent=parentFragment as MealContainerFragment
            parent.replaceDetailsWithEdit()
        }
        binding.yt.setOnClickListener{
            openYoutubeLink(getYouTubeId(ytLink)!!)
        }
    }

    private fun renderState(state: MealDetailsState) {
        when (state) {
            is MealDetailsState.Success -> {
                showLoadingState(false)
                populateView(state.mealDetails)
            }
            is MealDetailsState.Error -> {
                showLoadingState(false)
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is MealDetailsState.DataFetched -> {
                showLoadingState(false)
                Toast.makeText(context, "Fresh data fetched from the server", Toast.LENGTH_LONG).show()
            }
            is MealDetailsState.Loading -> {
                showLoadingState(true)
            }
        }
    }

    private fun populateView(meal: MealDetails){
        Glide.with(this)
            .load(meal.image)
            .fitCenter()
            .circleCrop()
            .into(binding.imageView2)
        binding.title.text=meal.name
        binding.tags.text="${meal.category} ${meal.area} ${meal.tags}"
        val ingredients=meal.ingredients

        val list = mutableListOf<String>()
        for(i in ingredients){
            list.add(i.key+" : "+i.value)
        }
        val ingredientsAdapter:ArrayAdapter<String> = ArrayAdapter(context!!,android.R.layout.simple_list_item_1,list.toTypedArray())
        binding.ingredientList.adapter=ingredientsAdapter
        ytLink= meal.youtube!!
        binding.recipe.text=meal.recipe
        binding.yt.text=meal.youtube
    }

    private fun showLoadingState(loading: Boolean) {
        binding.imageView2.isVisible=!loading
        binding.title.isVisible=!loading
        binding.tags.isVisible=!loading
        binding.ingredients.isVisible=!loading
        binding.ingredientList.isVisible=!loading
        binding.recipeTitle.isVisible=!loading
        binding.recipe.isVisible=!loading
        binding.floatingActionButton2.isVisible=!loading
        binding.loadingPb.isVisible = loading
    }

    fun openYoutubeLink(youtubeID: String) {
        val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubeID))
        val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + youtubeID))
        try {
            this.startActivity(intentApp)
        } catch (ex: ActivityNotFoundException) {
            this.startActivity(intentBrowser)
        }
    }

    private fun getYouTubeId(youTubeUrl: String): String? {
        val pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern = Pattern.compile(pattern)
        val matcher: Matcher = compiledPattern.matcher(youTubeUrl)
        return if (matcher.find()) {
            matcher.group()
        } else {
            "error"
        }
    }

}