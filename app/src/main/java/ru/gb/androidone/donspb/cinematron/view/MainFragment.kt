package ru.gb.androidone.donspb.cinematron.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.gb.androidone.donspb.cinematron.Consts
import ru.gb.androidone.donspb.cinematron.R
import ru.gb.androidone.donspb.cinematron.databinding.MainFragmentBinding
import ru.gb.androidone.donspb.cinematron.model.MovieListItem
import ru.gb.androidone.donspb.cinematron.viewmodel.AppState
import ru.gb.androidone.donspb.cinematron.viewmodel.MainViewModel
import ru.gb.androidone.donspb.cinematron.viewmodel.MovieListsEnum

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val adapterRecent = movieRecycler()
    private val adapterNow = movieRecycler()
    private val adapterTop = movieRecycler()
    private val adapterPop = movieRecycler()
    private val adapterUp = movieRecycler()

    private fun movieRecycler() = MovieRecycler(object : OnItemViewClickListener {
        override fun onItemViewClick(movie: MovieListItem) {
            val manager = activity?.supportFragmentManager?.apply {
                beginTransaction()
                .replace(R.id.main_container, MovieFragment.newInstance(Bundle().apply {
                    putInt(Consts.BUNDLE_ID_NAME, movie.id)
                }))
                .addToBackStack("")
                .commitAllowingStateLoss()
            }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            movieRecycler.adapter = adapterRecent
            movieRecyclerNow.adapter = adapterNow
            movieRecyclerTop.adapter = adapterTop
            movieRecyclerPop.adapter = adapterPop
            movieRecyclerUp.adapter = adapterUp
        }

        viewModel.movieListData.observe(viewLifecycleOwner, Observer { renderData(it,"") })
        viewModel.movieListDataNow.observe(viewLifecycleOwner, Observer {
            renderData(it, MovieListsEnum.NowPlayingList.listName) })
        viewModel.movieListDataPop.observe(viewLifecycleOwner, Observer {
            renderData(it, MovieListsEnum.PopularList.listName) })
        viewModel.movieListDataTop.observe(viewLifecycleOwner, Observer {
            renderData(it, MovieListsEnum.TopRatedList.listName) })
        viewModel.movieListDataUp.observe(viewLifecycleOwner, Observer {
            renderData(it, MovieListsEnum.UpcomingList.listName) })
        viewModel.getMovieListFromRemote(MovieListsEnum.NowPlayingList)
        viewModel.getMovieListFromRemote(MovieListsEnum.PopularList)
        viewModel.getMovieListFromRemote(MovieListsEnum.TopRatedList)
        viewModel.getMovieListFromRemote(MovieListsEnum.UpcomingList)

    }

    private fun renderData(appState: AppState, listname: String) {
        when (appState) {
            is AppState.Starting -> {
                binding.rvLoadingLayout.visibility = View.GONE
                binding.mainScrollview.visibility = View.VISIBLE
                when (listname) {
                    "Now Playing" -> {
                        adapterNow.setMovie(appState.movieList.results)
                    }
                    "Popular" -> {
                        adapterPop.setMovie(appState.movieList.results)
                    }
                    "Top Rated" -> {
                        adapterTop.setMovie(appState.movieList.results)
                    }
                    "Upcoming" -> {
                        adapterUp.setMovie(appState.movieList.results)
                    }
                }
            }
            is AppState.Loading -> {
                binding.mainScrollview.visibility = View.GONE
                binding.rvLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.mainScrollview.showSnackBar(getString(R.string.error))
            }
        }
    }

    override fun onDestroyView() {
        adapterNow.removeListener()
        adapterRecent.removeListener()
        adapterTop.removeListener()
        adapterPop.removeListener()
        adapterUp.removeListener()
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(movie: MovieListItem)
    }
}

private fun View.showSnackBar(
    text: String,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).show()
}