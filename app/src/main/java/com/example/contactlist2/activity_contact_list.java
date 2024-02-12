package com.example.contactlist2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class activity_contact_list extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        initListButton();
        initListsButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Getting the sort data before creating the contacts ArrayList
        String sortBy = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortfield", "contactname");
        String sortOrder = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");
        ContactDataSource ds = new ContactDataSource(this);
        ArrayList<Contact> contacts;

        try {
            ds.open();
            contacts = ds.getContacts(sortBy, sortOrder);
            ds.close();
            RecyclerView contactList = findViewById(R.id.rvContacts);
            ContactAdapter contactAdapter = new ContactAdapter(contacts, activity_contact_list.this);


            //If there are no contacts, open the Main Activity rather than contact list first
            if (contacts.size() > 0) {
                contactList = findViewById(R.id.rvContacts);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                contactList.setLayoutManager(layoutManager);
                contactList.setAdapter(contactAdapter);
            } else {
                Intent intent = new Intent(activity_contact_list.this, MainActivity.class);
                startActivity(intent);
            }


            // Added the ItemClickListener here for the class contact to have been initialized
            //Book said to add it before the onCreate method
            View.OnClickListener onItemClickListener = new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                    int position = viewHolder.getAdapterPosition();
                    int contactId = contacts.get(position).getContactID();
                    Intent intent = new Intent(activity_contact_list.this, MainActivity.class);
                    intent.putExtra("contactID", contactId);
                    startActivity(intent);
                }
            };
            contactAdapter.setmOnClickListener(onItemClickListener);

            //Switch turns delete buttons on and off
            //Book did not say to put here but the contactAdapter is here
            Switch s = findViewById(R.id.switchDelete);
            s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    Boolean status = compoundButton.isChecked();
                    contactAdapter.setDelete(status);
                    contactAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e){
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
        }
    }



    //Navigation to maps and to settings
    private void initListButton() {
        ImageButton mapButton = findViewById(R.id.mapBtn);
        ImageButton settingsButton = findViewById(R.id.settingsBtn);
        Button addButton = findViewById(R.id.addBtn);

        mapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(activity_contact_list.this, MapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears the stack trace
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(activity_contact_list.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears the stack trace
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(activity_contact_list.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears the stack trace
                startActivity(intent);
            }
        });
    }

    private void initListsButton() {
        ImageButton listBtn = findViewById(R.id.contactBtn);
        listBtn.setEnabled(false);
    }

}