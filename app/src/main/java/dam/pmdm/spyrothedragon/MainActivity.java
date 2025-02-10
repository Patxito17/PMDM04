package dam.pmdm.spyrothedragon;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

import dam.pmdm.spyrothedragon.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController = null;
    private NavController navGuideController = null;
    private boolean guideWatched;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar Toolbar
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navController = NavHostFragment.findNavController(navHostFragment);
            NavigationUI.setupWithNavController(binding.navView, navController);
            NavigationUI.setupActionBarWithNavController(this, navController);
        }

        binding.navView.setOnItemSelectedListener(this::selectedBottomMenu);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Si se navega a una pantalla donde se desea mostrar la flecha de atrás, habilítala
            getSupportActionBar().setDisplayHomeAsUpEnabled(destination.getId() != R.id.navigation_characters &&
                    destination.getId() != R.id.navigation_worlds &&
                    destination.getId() != R.id.navigation_collectibles);
        });

        guideWatched = getPreferences(MODE_PRIVATE).getBoolean("guideWatched", false);
        // Para mostrar siempre la guía descomentar la siguiente línea
        guideWatched = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        playSound(this, R.raw.gem_sound);
        if (!guideWatched) {
            startGuide();
            getPreferences(MODE_PRIVATE).edit().putBoolean("guideWatched", true).apply();
        } else {
            binding.includeLayout.guideIntroduction.setVisibility(View.GONE);
            binding.fullScreenFragmentContainer.setVisibility(View.GONE);
        }
    }

    private void startGuide() {
        ImageView logoSpyro = binding.getRoot().findViewById(R.id.logoSpyro);
        logoSpyro.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_rotate_in));

        ImageView diamondImage = binding.getRoot().findViewById(R.id.diamondImage);
        diamondImage.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_pulse));

        diamondImage.setOnClickListener(v -> {
            playSound(this, R.raw.spyro_sheep);
            v.clearAnimation();
            binding.includeLayout.guideIntroduction.setVisibility(View.GONE);
            binding.fullScreenFragmentContainer.setVisibility(View.VISIBLE);

            navGuideController = NavHostFragment.findNavController(
                    Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.fullScreenFragmentContainer))
            );
            navGuideController.navigate(R.id.navigation_guide);
        });
        // Se libera el espacio de memoria
        navGuideController = null;
    }

    private boolean selectedBottomMenu(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_characters)
            if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.navigation_worlds)
                navController.navigate(R.id.action_navigation_worlds_to_navigation_characters);
            else
                navController.navigate(R.id.action_navigation_collectibles_to_navigation_characters);
        else if (menuItem.getItemId() == R.id.nav_worlds)
            if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.navigation_characters)
                navController.navigate(R.id.action_navigation_characters_to_navigation_worlds);
            else
                navController.navigate(R.id.action_navigation_collectibles_to_navigation_worlds);
        else if (menuItem.getItemId() == R.id.nav_collectibles)
            if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.navigation_characters)
                navController.navigate(R.id.action_navigation_characters_to_navigation_collectibles);
            else
                navController.navigate(R.id.action_navigation_worlds_to_navigation_collectibles);
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gestiona el clic en el ítem de información
        if (item.getItemId() == R.id.action_info) {
            showInfoDialog();  // Muestra el diálogo
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInfoDialog() {
        // Crear un diálogo de información
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_about)
                .setMessage(R.string.text_about)
                .setPositiveButton(R.string.accept, null)
                .show();
    }

    public ActivityMainBinding getBinding() {
        return binding;
    }

    public static void playSound(Context context, int soundId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, soundId);
        mediaPlayer.start();
    }
}