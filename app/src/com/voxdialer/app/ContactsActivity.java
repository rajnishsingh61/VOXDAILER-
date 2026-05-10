package com.voxdialer.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.text.Editable;
import android.text.TextWatcher;
import android.os.Handler;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContactsActivity extends Activity {

    private List<Contact> allContacts;
    private List<Contact> filteredContacts;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        
        final View skeleton = findViewById(R.id.skeleton_container);
        final ListView list = findViewById(R.id.contacts_list);
        final EditText searchBar = findViewById(R.id.search_contacts);

        initMockData();
        filteredContacts = new ArrayList<>(allContacts);
        adapter = new ContactAdapter();
        list.setAdapter(adapter);

        // Simulate network delay for skeleton loading
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                skeleton.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);
            }
        }, 1500);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void initMockData() {
        allContacts = new ArrayList<>();
        allContacts.add(new Contact("Anonymous Rabbit", "+1 555 0101"));
        allContacts.add(new Contact("Cyber Ghost", "+1 555 0202"));
        allContacts.add(new Contact("Dark Matter", "+1 555 0303"));
        allContacts.add(new Contact("Echo Protocol", "+1 555 0404"));
        allContacts.add(new Contact("Frost Byte", "+1 555 0505"));
        allContacts.add(new Contact("Glitch Weaver", "+1 555 0606"));
    }

    private void filter(String query) {
        filteredContacts.clear();
        if (query.isEmpty()) {
            filteredContacts.addAll(allContacts);
        } else {
            String lowerQuery = query.toLowerCase(Locale.getDefault());
            for (Contact c : allContacts) {
                if (c.name.toLowerCase(Locale.getDefault()).contains(lowerQuery) || 
                    c.number.contains(lowerQuery)) {
                    filteredContacts.add(c);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private class Contact {
        String name;
        String number;
        Contact(String name, String number) {
            this.name = name;
            this.number = number;
        }
    }

    private class ContactAdapter extends BaseAdapter {
        @Override
        public int getCount() { return filteredContacts.size(); }
        @Override
        public Object getItem(int position) { return filteredContacts.get(position); }
        @Override
        public long getItemId(int position) { return position; }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(ContactsActivity.this).inflate(R.layout.item_contact, parent, false);
            }
            Contact contact = filteredContacts.get(position);
            TextView name = convertView.findViewById(R.id.contact_name);
            TextView number = convertView.findViewById(R.id.contact_number);
            name.setText(contact.name);
            number.setText(contact.number);
            return convertView;
        }
    }
}
