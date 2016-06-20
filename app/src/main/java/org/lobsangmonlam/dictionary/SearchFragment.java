package org.lobsangmonlam.dictionary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements TextWatcher, MonlamConstants {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private int dbresid;
    private String db;


    private ListView lv;
    private TextView tv;

    private SimpleCursorAdapter adapter;
    private Cursor cursor;

    private DBAdapter database;
    //String[] cols = new String[] { COLUMN_WORD, COLUMN_MEANING };
    //int[] names = new int[] {R.id.WORD, R.id.MEANING};
    String[] cols = new String[] { COLUMN_WORD};
    int[] names = new int[] {R.id.WORD};


    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param db Parameter 1.
     * @param dbresid Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    public static SearchFragment newInstance(String db, int dbresid) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt("dbresid", dbresid);
        args.putString("db", db);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            db = getArguments().getString("db");
            dbresid = getArguments().getInt("dbresid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.searchtab, container, false);

        try
        {
            File dbPath = new File(Environment.getExternalStorageDirectory(),DB_FOLDER_NAME);

            // Call the database adapter to create the database
            database = new DBAdapter(getActivity(), dbresid, dbPath, db, db);

            tv = (TextView)view.findViewById(R.id.searchbox);
            tv.addTextChangedListener(this);

            if (db.equals("entotb"))
                tv.setHint(getString(R.string.searchhint_en));

            lv = (ListView)view.findViewById(R.id.mylist);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    cursor.moveToPosition(position);
                    String word = cursor.getString(1);
                    String meaning = cursor.getString(2);

                    Intent intent = new Intent(getActivity(),EntryActivity.class);
                    intent.putExtra("word",word);
                    intent.putExtra("meaning",meaning);
                    getActivity().startActivity(intent);
                }
            });

            lv.setTextFilterEnabled(true);

            View button = view.findViewById(R.id.BTN_SEARCH);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new QueryTask().execute(tv.getText().toString());

                }
            });
        }
        catch (Exception e)
        {
            Log.e("db",e.getLocalizedMessage(),e);
        }

        return view;
    }

    /**
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void afterTextChanged(Editable s) {

        new QueryTask().execute(s.toString());


    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            adapter = new SimpleCursorAdapter(getActivity(), R.layout.listitemtitle,cursor,cols,names);

            // Set the ListView
            lv.setAdapter(adapter);
            lv.setTextFilterEnabled(true);
        }
    };


    private class QueryTask extends AsyncTask<String, Void, Void> {

        // can use UI thread here
        protected void onPreExecute() {

        }

        // automatically done on worker thread (separate from UI thread)
        protected Void doInBackground(final String... args) {

            try
            {
                String queryString = args[0];

                String queryText = COLUMN_WORD + " LIKE '" + queryString + "%'";

                //OR " + COLUMN_MEANING + " LIKE '%" + queryString + "%'";

                cursor = database.getAllEntries(new String[] {COLUMN_ID, COLUMN_WORD, COLUMN_MEANING}, queryText, null, null, null, COLUMN_WORD, " ASC LIMIT " + QUERY_LIMIT);

                Log.d("db","found items: " + cursor.getCount());

                getActivity().startManagingCursor(cursor);

                handler.sendMessage(new Message());

            }
            catch (Exception e)
            {
                Log.e("db",e.getLocalizedMessage(),e);
            }

            return null;
        }

        // can use UI thread here
        protected void onPostExecute(final Void unused) {

        }
    }




}
