package ru.gb.androidone.donspb.cinematron.viewmodel

import ru.gb.androidone.donspb.cinematron.R

enum class MovieListsEnum(val pathPart: String, val listName: String) {
    NowPlayingList("now_playing", "Now Playing"),
    PopularList("popular", "Popular"),
    TopRatedList("top_rated", "Top Rated"),
    UpcomingList("upcoming", "Upcoming")
}