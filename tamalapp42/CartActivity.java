package mejia.oscar.tamalapp42;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mejia.oscar.tamalapp42.Model.Cart;
import mejia.oscar.tamalapp42.Prevalent.Prevalent;
import mejia.oscar.tamalapp42.ViewHolder.CartViewHolder;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Button NextProcessBtn;
    private TextView txtTotalAmount, txtMsg1;

    private int overTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_cart );
        recyclerView = findViewById( R.id.cart_list );
        recyclerView.setHasFixedSize( true );
        layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );

        NextProcessBtn = findViewById( R.id.next_btn );
        txtTotalAmount = findViewById( R.id.total_price );
        txtMsg1 =  findViewById(R.id.msg1);









        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //  txtTotalAmount.setText("Total Price = $" + (overTotalPrice));

                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onStart() {


        super.onStart();

        CheckOrderState();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef.child("User View")
                .child( Prevalent.currentOnlineUser.getId())
                .child("Products"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final  Cart model) {

                holder.txtProductQuantity.setText("Cantidad = " + model.getQuantity() );
                holder.txtProductPrice.setText( "Precio =" + model.getPrice() );
                holder.txtProductName.setText(  model.getPname() );

                int oneTyprProductTPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                overTotalPrice = overTotalPrice + oneTyprProductTPrice;



                txtTotalAmount.setText("Precio Total = $" + (overTotalPrice));






                holder.itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]{
                                "Editar", "Borrar"

                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder( CartActivity.this );
                        builder.setTitle("Cart Options:");
                        builder.setItems( options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if(i == 0 ){
                                    Intent intent99 = new Intent( CartActivity.this, ProductDetailsActivity.class );
                                    intent99.putExtra( "pid", model.getPid() );
                                    startActivity( intent99 );
                                }
                                if(i == 1)
                                {
                                    cartListRef.child("User View").child(Prevalent.currentOnlineUser.getId())
                                            .child("Products").child(model.getPid()).removeValue().addOnCompleteListener( new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                Toast.makeText(CartActivity.this, "se ha borrado el artículo correctamente", Toast.LENGTH_SHORT ).show();
                                                Intent intent9 = new Intent( CartActivity.this, HomeGoodActivity.class );
                                                intent9.putExtra( "pid", model.getPid() );
                                                startActivity( intent9 );
                                            }
                                        }
                                    } );

                                }

                            }
                        } );

                        builder.show();
                    }
                } );
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false );
                CartViewHolder holder  = new CartViewHolder( view );
                return holder;
            }
        };

        recyclerView.setAdapter( adapter );
        adapter.startListening();




    }




    private void CheckOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getId());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                DatabaseReference ordersRef;
                ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child( Prevalent.currentOnlineUser.getId());



                ordersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {/*
                if (dataSnapshot.exists())
                {
                    String shippingState = dataSnapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped"))
                    {
                        state = "Order Shipped";
                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        state = "Order Placed";
                    }
                }

                */
                        if (dataSnapshot.exists())
                        {
                            String shippingState = dataSnapshot.child("state").getValue().toString();
                            String userName = dataSnapshot.child("name").getValue().toString();

                            if (shippingState.equals("shipped"))
                            {
                            }
                            else if(shippingState.equals("not shipped"))
                            {
                                Intent intent = new Intent(CartActivity.this, UserUserProductsActivity.class);
                                startActivity(intent);





                            }
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





}