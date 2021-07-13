Regional Countries is an android app made using Java to display information about countries in region by consuming a rest api and persistantly storing most of the data to display when the user is offline.

The following attributes of a country(if found) are displayed - 
1. name
2. capital
3. flag(image)
4. region
5. subregion
6. population
7. borders
8. languages

The app has a search bar to search for countries by (valid)region. By default(on start) it showsall countries in region Asia.

The app has a toolbar menu option for clearing the backend sqlite database. It does not clear the countries currently in running program.

The app has a toolbar menu option for refreshing the screen.

Note:
1. GlideToVector library has been used to decode svg image files.
2. Glide librariy have been used to download, cache and temporarily persist all image data
3. Only text/String data has been stored using Room
