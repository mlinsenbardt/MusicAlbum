MusicAlbum
==========

This is simple Android app for arranging sheet music.

The Main Activity is a basic List Item view, with each item corresponding to an arrangement
on the local database. With a single-click,the user will be taken to an ImageView where they
will be able to view the sheets. A long-click will bring up a context menu, allowing the user
to either edit or remove the arrangement.

To create a new arrangement, the user should click the "NewAlbum" button on the ActionBar. 
This will redirect the user to an AlertDialog menu, prompting them for a title, a composer,
and a notes section for their arrangement. The title field is required for all arrangements.

After the user completes this form, they will be redirected to their devices' camera activity.
They will then be able to take multiple pictures of sheets. When the user is through taking 
pictures, they will return to the main activity, where they should be able to see their new
arrangement as an item in the list. Some devices may need to reload the app for this to show
up.

##License

This app is licensed under the [GNU General Public License] (http://opensource.org/licenses/GPL-2.0.php)
