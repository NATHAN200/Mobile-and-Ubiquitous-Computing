package coursework.coursework;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Nicholas on 18/12/2014.
 */
public class AboutDialog extends DialogFragment {

    //Called when we want to create a new dialog
    public static AboutDialog newInstance(String title) {
        //Make a new AboutDialog object
        AboutDialog frag = new AboutDialog();
        //Create a bundle and place the message inside
        Bundle args = new Bundle();
        args.putString("text", title);
        frag.setArguments(args);
        return frag;
    }


    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder aDialog = new AlertDialog.Builder(getActivity());
        //Set the message of the dialog
        aDialog.setMessage(getArguments().getString("text"))
                .setPositiveButton("OK",new DialogInterface.OnClickListener()
                {
                    //set the on click listener
                    public void onClick(DialogInterface dialog, int id)
                    {

                    }
                });
        aDialog.setTitle("About");  //Set the title
        aDialog.setIcon(R.drawable.abc_ic_search);  //set the Icon
        return aDialog.create();
    }

}
