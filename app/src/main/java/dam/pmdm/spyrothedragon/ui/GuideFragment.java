package dam.pmdm.spyrothedragon.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import dam.pmdm.spyrothedragon.MainActivity;
import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.databinding.FragmentGuideBinding;

public class GuideFragment extends Fragment {

    private FragmentGuideBinding binding;
    private static final String TAG = "GuideFragment";
    private MainActivity mainActivity;
    private int clickCount = 0;

    public GuideFragment() {
    }

    public static GuideFragment newInstance() {
        return new GuideFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGuideBinding.inflate(inflater, container, false);
        mainActivity = (MainActivity) getActivity();
        int statusBarHeight = getStatusBarHeight();
        RoundedRectangleView rectangleView = binding.getRoot().findViewById(R.id.rounded_rectangle);
        MainActivity.playSound(getActivity(), R.raw.gem_sound);

        // Recorre los diferentes pasos de la guía
        showNextStep(statusBarHeight, rectangleView, R.id.nav_characters);

        binding.button.setOnClickListener(v -> {
            // Al pulsar el botón se cierra la guía
            MainActivity.playSound(getActivity(), R.raw.spyro_sheep);
            closeGuide();
        });

        rectangleView.setOnClickListener(v -> {
            switch (clickCount) {
                case 0: // Mundos
                    clickCount++;
                    mainActivity.getBinding().navView.setSelectedItemId(R.id.nav_worlds);
                    showNextStep(statusBarHeight, rectangleView, R.id.nav_worlds);
                    break;
                case 1: // Coleccionables
                    clickCount++;
                    mainActivity.getBinding().navView.setSelectedItemId(R.id.nav_collectibles);
                    showNextStep(statusBarHeight, rectangleView, R.id.nav_collectibles);
                    break;
                case 2: // Icono info
                    clickCount++;
                    binding.button.setText(R.string.guide_end);
                    showNextStep(statusBarHeight, rectangleView, R.id.action_info);
                    break;
                case 3: // Resumen de la guía
                    MainActivity.playSound(getActivity(), R.raw.gem_sound);
                    binding.includeLayout.guideResume.setVisibility(View.VISIBLE);

                    ImageView logoSpyro = binding.getRoot().findViewById(R.id.logoSpyro);
                    logoSpyro.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale_rotate_in));

                    ImageView diamondImage = binding.getRoot().findViewById(R.id.diamondImage);
                    diamondImage.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale_pulse));

                    diamondImage.setOnClickListener(view -> {
                        MainActivity.playSound(getActivity(), R.raw.spyro_3_sign);
                        view.clearAnimation();
                        closeGuide();
                    });
                    break;
            }
        });

        return binding.getRoot();
    }

    private void closeGuide() {
        binding.includeLayout.guideResume.setVisibility(View.GONE);
        mainActivity.getBinding().fullScreenFragmentContainer.setVisibility(View.GONE);
        mainActivity.getBinding().includeLayout.guideIntroduction.setVisibility(View.GONE);
        if (clickCount > 0)
            mainActivity.getBinding().navView.setSelectedItemId(R.id.nav_characters);
        getParentFragmentManager().beginTransaction().remove(this).commit();
    }

    private void showNextStep(int statusBarHeight, RoundedRectangleView rectangleView, int idView) {
        MainActivity.playSound(getActivity(), R.raw.gem_sound);

        if (rectangleView.getAnimation() != null && rectangleView.getAnimation().isInitialized()) {
            rectangleView.getAnimation().cancel();
        }
        rectangleView.setVisibility(View.INVISIBLE);
        switch (clickCount) {
            case 0: // Personajes
                binding.text.setText(getString(R.string.guide_text_step2) + getString(R.string.guide_text_next));
                break;
            case 1: // Mundos
                binding.text.setText(getString(R.string.guide_text_step3) + getString(R.string.guide_text_next));
                break;
            case 2: // Coleccionables
                binding.text.setText(getString(R.string.guide_text_step4) + getString(R.string.guide_text_next));
                break;
            case 3: // Icono info
                binding.text.setText(getString(R.string.guide_text_step5));
                break;
        }

        // Animación de las vistas
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(binding.text, "scaleX", 0f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(binding.text, "scaleY", 0f, 1.0f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(binding.button, "alpha", 0f, 1.0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, alpha);
        animatorSet.setDuration(1000);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                rectangleView.setVisibility(View.VISIBLE);
                showRectangleStep(statusBarHeight, rectangleView, idView);
            }
        });
    }

    private void showRectangleStep(int statusBarHeight, RoundedRectangleView rectangleView, int idView) {
        // Obtener coordenadas y tamaño del elemento a resaltar
        View targetView = getActivity().findViewById(idView);
        int[] location = new int[2];
        targetView.getLocationOnScreen(location);
        float targetX = location[0];
        float targetY = location[1];
        float targetWidth = targetView.getWidth();
        float targetHeight = targetView.getHeight();

        // Definir el tamaño y posición del resaltado
        float right = targetX + targetWidth;
        float bottom = targetY + targetHeight - statusBarHeight;

        // Actualizar las propiedades de la vista en función del elemento a resaltar
        rectangleView.setRectangleBounds(targetX, targetY - statusBarHeight, right, bottom);

        // Animación de resaltado
        rectangleView.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in));
    }

    private int getStatusBarHeight() {
        int result = 0;
        // No he encontrado una forma no obsoleta de obtener el tamaño de la barra de estado
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}