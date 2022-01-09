package mejia.oscar.tamalapp42;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;
import mejia.oscar.tamalapp42.Model.Products;
import mejia.oscar.tamalapp42.Model.Users;
import mejia.oscar.tamalapp42.Prevalent.Prevalent;
import mejia.oscar.tamalapp42.ViewHolder.ProductViewHolder;

public class HomeActivity extends AppCompatActivity {
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    private DatabaseReference mDatabaseReference;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );

        Paper.init( this );
        ProductsRef = FirebaseDatabase.getInstance().getReference().child( "Products" );

FloatingActionButton fab = findViewById( R.id.fab );
fab.setOnClickListener( new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(HomeActivity.this, CartActivity.class);
        startActivity(intent);
    }
} );
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById( R.id.recycle_menu );
        recyclerView.setHasFixedSize( true );
        layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );


    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery( ProductsRef, Products.class )
                        .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>( options ) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText( model.getPname() );
                        holder.txtProductDescription.setText( model.getDescription() );
                        holder.txtProductPrice.setText( "Precio = " + model.getPrice() + "$" );
                        Picasso.get().load( model.getImage() ).into( holder.imageView );


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {


                                    Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.product_items_layout, parent, false );
                        ProductViewHolder holder = new ProductViewHolder( view );
                        return holder;
                    }
                };
        recyclerView.setAdapter( adapter );
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Paper.book().destroy();

        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();


    }

}
