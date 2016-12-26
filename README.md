# Popcorn Club - It's all about entertainment

Popcorn Club is the best way to discover everything about your favorite movies and TV shows, watch trailers, check showtimes, photos, cast and crew. Users can easily rate a movie, manage favorites, create a watchlist and share with friends.

Supported features:
* Search
* Movie, TV show, person details
* Share items
* Watch trailers
* Account login
* Rating a movie
* Manage your favourites
* Manage your watch list
* Supports multiple languages

**Developed by:** [_Ravi Kumar Mandala_](https://github.com/ravimandala)

**User Stories: v1.0**
* [ ] Required: Display a grid of top movies posters sortable based on popularity or rating.
* [ ] Required: User can tap on a movie to launch movie details screen with title, release date, movie poster, vote average, and plot synopsis.
* [ ] Required: Network Manager - In a background thread, app queries the `/movie/popular` or `/movie/top_rated` API for the sort criteria specified in the settings menu.
* [ ] Required: App conforms to common standards found in the [Android Nanodegree General Project Guidelines](http://udacity.github.io/android-nanodegree-guidelines/core.html).
* [ ] Optional: Restore the data using onSaveInstanceState/onRestoreInstanceState.
* [ ] Optional: Upload application to [TMDb app directory](https://www.themoviedb.org/account/ravimandala/api/directory).

**Nitty-gritty**
* [x] Use a StaggeredGridLayout with additional decorations.
* [ ] Use ButterKnife for view injection.
* [ ] Properly handle the absence of connectivity (Picasso would suffer silently otherwise).

**User Stories: v2.0**
* [ ] Required: Add more information to movie details view
 * [ ] Allow users to view and play trailers (either in the youtube app or a web browser).
 * [ ] Allow users to read reviews of a selected movie.
 * [ ] Allow users to mark a movie as a favorite in the details view by tapping a (star) button. (This is for a local movies collection that you will maintain and does not require an API request).
* [ ] Required: Modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.
* [ ] Optional: Optimize the app experience for tablet.

### Notes:
* Created API key on TMDb.

Walkthrough GIFs:

![Video Walkthrough](app/src/main/res/drawable/anim_basic_tudu.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).