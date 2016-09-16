package com.chhd.mobliebutler.biz;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;

import com.chhd.mobliebutler.entity.Contact;
import com.chhd.mobliebutler.global.Consts;
import com.chhd.mobliebutler.util.Tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by CWQ on 2016/8/28.
 */
public class ContactBiz implements Consts {

    private Context context;
    private Handler handler;

    public ContactBiz(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public void getContacts() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    List<Contact> contacts = new ArrayList<>();
                    ContentResolver contentResolver = context.getContentResolver();
                    Cursor contactsCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts.NAME_RAW_CONTACT_ID}, null, null, null);
                    while (contactsCursor.moveToNext()) {
                        String id = contactsCursor.getString(0);
                        Cursor dataCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1}, ContactsContract.Data.RAW_CONTACT_ID + "= ?", new String[]{id}, null);
                        while (dataCursor.moveToNext()) {
                            String number = null;
                            String mimetype = dataCursor.getString(0);
                            String data1 = dataCursor.getString(1);
                            if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                                String name = null;
                                String letter = null;
                                number = data1.replaceAll("[\\s\\-]", "");
                                Cursor dataCursor2 = contentResolver.query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1}, ContactsContract.Data.RAW_CONTACT_ID + "= ?", new String[]{id}, null);
                                while (dataCursor2.moveToNext()) {
                                    mimetype = dataCursor2.getString(0);
                                    data1 = dataCursor2.getString(1);
                                    if (mimetype.equals("vnd.android.cursor.item/name")) {
                                        name = data1;
                                        letter = Tools.getLetter(name);
                                    }
                                }
                                if (name == null) {
                                    letter = "#";
                                }
                                dataCursor2.close();
                                contacts.add(new Contact(name, letter, number));
                            }
                        }
                        dataCursor.close();
                    }
                    contactsCursor.close();
                    Collections.sort(contacts, new InnerComparator());
                    Message message = Message.obtain();
                    message.what = MSG_GET_CONTACTS;
                    message.obj = contacts;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private class InnerComparator implements Comparator<Contact> {
        @Override
        public int compare(Contact lhs, Contact rhs) {
            return lhs.getLetter().compareTo(rhs.getLetter());
        }
    }
}
