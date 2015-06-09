package com.kit.yinp.imgtagtest;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kit.yinp.imgtagtest.view.ImgTagView;
import com.kit.yinp.imgtagtest.view.ScreenUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;


public class MainActivity extends FragmentActivity {
    ImageView img;
    ImgTagView tagView;
    ImgTagView tagView1;

    Button switchModeBtn;
    Button editBtn;
    private boolean isEdit;
    private EditTagFragment editTagFragment;

    private RelativeLayout containerRel;
    RelativeLayout.LayoutParams lp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.img_progress_img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击让 tagView显示不显示
                if(!isEdit)
                    backgroudClick();
            }
        });

        containerRel = (RelativeLayout) findViewById(R.id.img_process_container);





//        tagView = (ImgTagView) findViewById(R.id.img_process_tag_view);
//        tagView.setCanTouch(true);
        tagView = new ImgTagView(this);
        tagView.setCanTouch(true);

        lp = new RelativeLayout.LayoutParams((int)ScreenUtils.dpToPx(this,250), (int)ScreenUtils.dpToPx(this,90));
        lp.leftMargin = 200;
        lp.topMargin = 200;
        containerRel.addView(tagView,lp);


        switchModeBtn = (Button) findViewById(R.id.switch_mode_btn);
        editBtn = (Button) findViewById(R.id.to_edit_btn);

        switchModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEdit){
                    switchModeBtn.setText("展示模式");
                    editBtn.setVisibility(View.GONE);
                    toDisplayMode();
                    isEdit = false;
                }else{
                    switchModeBtn.setText("编辑模式");
                    editBtn.setVisibility(View.VISIBLE);
                    toEditMode();
                    isEdit = true;
                }
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toEdit();
            }
        });


        waveAnimate(tagView);

        //转到展示模式
        editBtn.setVisibility(View.GONE);
        toDisplayMode();
        isEdit = false;


    }



    private void toEdit(){
        if(editTagFragment == null){
            editTagFragment = EditTagFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.root_container,editTagFragment).commitAllowingStateLoss();
        }else{
            closeEditFragment();
            editTagFragment = EditTagFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.root_container,editTagFragment).commitAllowingStateLoss();

        }
    }


    public void closeEditFragment(){
        if(editTagFragment != null){
            getSupportFragmentManager().beginTransaction().hide(editTagFragment).remove(editTagFragment).commitAllowingStateLoss();

        }
    }


    public void setTags(String[] tags){
        tagView.setTags(tags);
    }


    private void toDisplayMode(){
        tagView.setCanTouch(false);

    }

    private void toEditMode(){
        tagView.setCanTouch(true);

    }


    private void backgroudClick(){
        if(tagView.isShown()){

            ObjectAnimator radiusScale = ObjectAnimator.ofFloat(tagView,"centerPicWidth",new float[]{0.8f,1.6f,1.0f});
            radiusScale.setDuration(600l);
            radiusScale.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    tagView.setDrawline(false);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    tagView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            radiusScale.start();



        }else{

            ObjectAnimator radiusScale = ObjectAnimator.ofFloat(tagView,"centerPicWidth",new float[]{0.8f,1.6f,1.0f});
            radiusScale.setDuration(600l);
            radiusScale.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    tagView.setVisibility(View.VISIBLE);
                    tagView.setDrawline(false);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    tagView.setDrawline(true);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            radiusScale.start();
        }
    }


    //tagView的波纹动画
    private void waveAnimate(View tagView) {
        ObjectAnimator aplha = ObjectAnimator.ofFloat(tagView,"waveAlpha",new float[]{0.6f,0.0f});
        float[] radiuss = new float[2];
        radiuss[0] = ScreenUtils.dpToPx(this,5.0f);
        radiuss[1] = ScreenUtils.dpToPx(this,18.0f);
        ObjectAnimator scale = ObjectAnimator.ofFloat(tagView,"waveRadius",radiuss);


        aplha.setRepeatMode(1);
        aplha.setRepeatCount(-1);
        scale.setRepeatMode(1);
        scale.setRepeatCount(-1);

        AnimatorSet aplhaScale = new AnimatorSet();
        aplhaScale.play(aplha).with(scale);
        aplhaScale.setStartDelay(1400L);
        aplhaScale.setDuration(900L);
        aplhaScale.setInterpolator(new AccelerateInterpolator(2.0f));
        aplhaScale.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
