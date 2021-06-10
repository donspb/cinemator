package ru.gb.androidone.donspb.cinematron.viewmodel

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.gb.androidone.donspb.cinematron.R
import ru.gb.androidone.donspb.cinematron.model.MovieList
import ru.gb.androidone.donspb.cinematron.repository.MainRepoImpl
import ru.gb.androidone.donspb.cinematron.repository.RemoteDataSource

class MainViewModel(
    val movieListData: MutableLiveData<AppState> = MutableLiveData(),
    val movieListDataNow: MutableLiveData<AppState> = MutableLiveData(),
    val movieListDataPop: MutableLiveData<AppState> = MutableLiveData(),
    val movieListDataTop: MutableLiveData<AppState> = MutableLiveData(),
    val movieListDataUp: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: MainRepoImpl = MainRepoImpl(RemoteDataSource())
) : ViewModel() {

//    private val listOfLists: MutableList<MovieList> = mutableListOf()

    fun getMovieListFromRemote(listType: MovieListsEnum) {
        when (listType.listName) {
            "Now Playing" -> {
                movieListDataNow.value = AppState.Loading
                repositoryImpl.getMovieListFromServer(listType.pathPart, callBackNow)
            }
            "Popular" -> {
                movieListDataPop.value = AppState.Loading
                repositoryImpl.getMovieListFromServer(listType.pathPart, callBackPop)
            }
            "Top Rated" -> {
                movieListDataTop.value = AppState.Loading
                repositoryImpl.getMovieListFromServer(listType.pathPart, callBackTop)
            }
            "Upcoming" -> {
                movieListDataUp.value = AppState.Loading
                repositoryImpl.getMovieListFromServer(listType.pathPart, callBackUp)
            }
        }
    }

    private val callBackNow = object : Callback<MovieList> {

        override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
            val serverResponse: MovieList? = response.body()
            movieListDataNow.postValue(
                if (response.isSuccessful && serverResponse!= null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(R.string.server_error.toString()))
                }
            )
        }

        override fun onFailure(call: Call<MovieList>, t: Throwable) {
            movieListDataNow.postValue(AppState.Error(Throwable(t.message ?: R.string.request_error.toString())))
        }

    }

    private val callBackPop = object : Callback<MovieList> {

        override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
            val serverResponse: MovieList? = response.body()
            movieListDataPop.postValue(
                    if (response.isSuccessful && serverResponse!= null) {
                        checkResponse(serverResponse)
                    } else {
                        AppState.Error(Throwable(R.string.server_error.toString()))
                    }
            )
        }

        override fun onFailure(call: Call<MovieList>, t: Throwable) {
            movieListDataPop.postValue(AppState.Error(Throwable(t.message ?: R.string.request_error.toString())))
        }

    }

    private val callBackTop = object : Callback<MovieList> {

        override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
            val serverResponse: MovieList? = response.body()
            movieListDataTop.postValue(
                    if (response.isSuccessful && serverResponse!= null) {
                        checkResponse(serverResponse)
                    } else {
                        AppState.Error(Throwable(R.string.server_error.toString()))
                    }
            )
        }

        override fun onFailure(call: Call<MovieList>, t: Throwable) {
            movieListDataTop.postValue(AppState.Error(Throwable(t.message ?: R.string.request_error.toString())))
        }

    }

    private val callBackUp = object : Callback<MovieList> {

        override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
            val serverResponse: MovieList? = response.body()
            movieListDataUp.postValue(
                    if (response.isSuccessful && serverResponse!= null) {
                        checkResponse(serverResponse)
                    } else {
                        AppState.Error(Throwable(R.string.server_error.toString()))
                    }
            )
        }

        override fun onFailure(call: Call<MovieList>, t: Throwable) {
            movieListDataUp.postValue(AppState.Error(Throwable(t.message ?: R.string.request_error.toString())))
        }

    }

    private fun checkResponse(serverResponse: MovieList) : AppState {
        return AppState.Starting(serverResponse)
    }


//    private fun checkResponse(serverResponse: MovieList): AppState {
//        listOfLists.add(serverResponse)
//        return if (listOfLists.size == MovieListsEnum.values().size) {
//            AppState.Starting(listOfLists)
//        } else {
//            AppState.Loading
//        }
//    }

//    private fun getDataFromLocalSource() {
//        movieListData.value = AppState.Loading
//
//        Thread {
//            sleep(1000)
//            movieListData.postValue(
//                    AppState.Starting(repositoryImpl.getMoviesLocalList(),
//                            repositoryImpl.getMoviesLocalList(),
//                            repositoryImpl.getMoviesLocalList())
//            )
//        }.start()
//    }
}