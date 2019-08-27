package com.smarteist.mrnews;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.Objects;

public abstract class BaseFragment<T extends BaseContracts.Presenter> extends Fragment implements BaseContracts.View<T>{


    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation = super.onCreateAnimation(transit, enter, nextAnim);

            if (animation == null && nextAnim != 0) {
                animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
            }

            if (animation != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Objects.requireNonNull(getView()).setLayerType(View.LAYER_TYPE_HARDWARE, null);
                }

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        //null
                    }

                    public void onAnimationEnd(Animation animation) {
                        try {
                            getView().setLayerType(View.LAYER_TYPE_NONE, null);
                        } catch (Exception e) {
                            Log.i("!!!", "onAnimationEnd: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        //null
                    }

                });
            }

        return animation;
    }

}
