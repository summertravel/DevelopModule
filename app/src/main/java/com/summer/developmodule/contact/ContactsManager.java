/**
 * Copyright 2017 ChenHao Dendi
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.summer.developmodule.contact;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ContactsManager {
    /**
     * @param context
     * @return List
     */
    @NonNull
    public static ArrayList<ShareContactsBean> getPhoneContacts(Context context) {
        ArrayList<ShareContactsBean> result = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor.getString(0).replace(" ", "").replace("-", "");
                String contactName = phoneCursor.getString(1);
                result.add(new ShareContactsBean(contactName, phoneNumber));
            }
            phoneCursor.close();
        }

        Collections.sort(result, new Comparator<ShareContactsBean>() {
            @Override
            public int compare(ShareContactsBean l, ShareContactsBean r) {
                return l.compareTo(r);
            }
        });
        return result;
    }
}

