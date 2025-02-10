package dam.pmdm.spyrothedragon.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.models.Character;
import dam.pmdm.spyrothedragon.ui.FireAnimationView;

public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder> {

    private List<Character> list;

    public CharactersAdapter(List<Character> charactersList) {
        this.list = charactersList;
    }

    @Override
    public CharactersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new CharactersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CharactersViewHolder holder, int position) {
        Character character = list.get(position);
        holder.nameTextView.setText(character.getName());

        // Cargar la imagen (simulado con un recurso drawable)
        int imageResId = holder.itemView.getContext().getResources().getIdentifier(character.getImage(), "drawable", holder.itemView.getContext().getPackageName());
        holder.imageImageView.setImageResource(imageResId);

        if (character.getName().equals("Spyro")) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ViewGroup parent = (ViewGroup) holder.itemView;

                    // Eliminar fuego/s anterior si está activado
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        View child = parent.getChildAt(i);
                        if (child instanceof FireAnimationView) {
                            parent.removeView(child);
                        }
                    }

                    // Crea la instancia del fuego
                    FireAnimationView fireAnimationView = new FireAnimationView(holder.itemView.getContext());
                    fireAnimationView.setId(View.generateViewId());

                    // Tamaño del fuego
                    int fireWidth = holder.imageImageView.getWidth() / 2;
                    int fireHeight = holder.imageImageView.getHeight() / 3;

                    fireAnimationView.setLayoutParams(new ViewGroup.LayoutParams(fireWidth, fireHeight));

                    // Posicionar el fuego
                    float mouthOffsetX = holder.imageImageView.getWidth() * 0.29f;
                    float mouthOffsetY = holder.imageImageView.getHeight() * 0.7f;

                    fireAnimationView.setX(holder.imageImageView.getX() + mouthOffsetX);
                    fireAnimationView.setY(holder.imageImageView.getY() + mouthOffsetY);

                    parent.addView(fireAnimationView);

                    // Rotar 180º cada fuego
                    fireAnimationView.setRotation(180f);

                    // Iniciar la animación del fuego
                    fireAnimationView.startAnimation();

                    // Animar hacia abajo
                    fireAnimationView.animate().translationYBy(25).setDuration(3000).start();
                    fireAnimationView.animate().alpha(0.9f).setDuration(3000).start();

                    // Avisar de activación del Easter Egg
                    Toast.makeText(holder.itemView.getContext(), "Easter Egg activado", Toast.LENGTH_SHORT).show();

                    // Ocultar después de 3 segundos
                    fireAnimationView.postDelayed(() -> parent.removeView(fireAnimationView), 3000);

                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CharactersViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageImageView;
        ImageView fireImageView;

        public CharactersViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            imageImageView = itemView.findViewById(R.id.image);
            fireImageView = itemView.findViewById(R.id.fire_animation);
        }
    }
}
