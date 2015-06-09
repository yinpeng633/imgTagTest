package com.kit.yinp.imgtagtest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by yinp on 2015/6/8.
 */
public class EditTagFragment extends Fragment {

    MainActivity activity;
    Button completeBtn;
    EditText et1;
    EditText et2;
    EditText et3;



    public static EditTagFragment newInstance(){
        EditTagFragment editTagFragment = new EditTagFragment();
        editTagFragment.setArguments(new Bundle());
        return editTagFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_tag,container,false);

        completeBtn = (Button) view.findViewById(R.id.edit_complete);
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != activity){
                    activity.closeEditFragment();
                    String tag1 = et1.getText().toString();
                    String tag2 = et2.getText().toString();
                    String tag3 = et3.getText().toString();

                    String[] tags = new String[]{tag1,tag2,tag3};
                    activity.setTags(tags);

                    //关闭软键盘
                    View peekDecorView = getActivity().getWindow().peekDecorView();
                    if (view != null) {
                        InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputmanger.hideSoftInputFromWindow(peekDecorView.getWindowToken(), 0);
                    }

                }
            }
        });

        et1 = (EditText) view.findViewById(R.id.detail_apply_register_name_et);
        et2 = (EditText) view.findViewById(R.id.detail_apply_register_phone_et);
        et3 = (EditText) view.findViewById(R.id.detail_apply_register_qq_et);




        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Called when a fragment is first attached to its activity.
     * {@link #onCreate(android.os.Bundle)} will be called after this.
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof MainActivity)
            this.activity = (MainActivity)activity;
    }
}
