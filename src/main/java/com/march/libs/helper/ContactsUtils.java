package com.march.libs.helper;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;

/**
 * CdLibsTest     com.march.libs.helper
 * Created by 陈栋 on 16/3/28.
 * 功能:
 */

public class ContactsUtils {
    /*************************************
     * 通讯录**********************************************
     * <p/>
     * 访问通讯录的uri ContactsContract.Contacts.CONTENT_URI
     * <p/>
     * ContactsContract.Contacts.DISPLAY_NAME_PRIMARY(display_name)
     * ContactsContract.Contacts.DISPLAY_NAME_ALTERNATIVE(display_name_alt)
     * ContactsContract.Contacts._ID(_id)
     * <p/>
     * **************************************电话*********************************
     * ******‘
     * <p/>
     * 访问电话的Uri ContactsContract.CommonDataKinds.Phone.CONTENT_URI
     * <p/>
     * ContactsContract.CommonDataKinds.Phone.DATA (data1)
     * ContactsContract.CommonDataKinds.Phone.MIMETYPE(mimetype)
     * <p/>
     * ContactsContract.CommonDataKinds.Email.RAW_CONTACT_ID(raw_contact_id)
     * ContactsContract.CommonDataKinds.Email.DATA(data1)
     * ContactsContract.CommonDataKinds.Email.MIMETYPE( mimetype)]
     * <p/>
     * Phone默认是相同的mimetype
     * <p/>
     * ContactsContract.CommonDataKinds.Email.CONTENT_TYPE(
     * vnd.android.cursor.dir/phone_v2)
     * ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE(
     * vnd.android.cursor.dir/phone_v2 )
     * <p/>
     * ************************************Email********************************
     * ****
     * <p/>
     * 查询Email的Uri ContactsContract.CommonDataKinds.Email.CONTENT_URI;
     * <p/>
     * ContactsContract.CommonDataKinds.Email.DATA(data1)
     * <p/>
     * ContactsContract.CommonDataKinds.Email.RAW_CONTACT_ID(raw_contact_id)
     * ContactsContract.CommonDataKinds.Email.DATA(data1)
     * ContactsContract.CommonDataKinds.Email.MIMETYPE( mimetype)
     * <p/>
     * email默认是相同的 mimetype
     * <p/>
     * ContactsContract.CommonDataKinds.Email.CONTENT_TYPE(
     * vnd.android.cursor.item/email_v2)
     * ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE(
     * vnd.android.cursor.dir/email_v2)
     */
    // 访问raw_contacts这张表的Uri
    private static Uri contactUri = Uri
            .parse("content://com.android.contacts/raw_contacts");
    public static String[] conColumn = {"_id", "display_name"};

    // 访问data这张表的Uri
    private static Uri dataUri = Uri
            .parse("content://com.android.contacts/data");
    public static String[] dataColumn = {"data1"};

    /*-----------------------------------向通讯录中查询数据------------------------------*/
    /* 查询所有信息 */
    public static ArrayList<InfoOfContacts> Select(Context context) {
        ArrayList<InfoOfContacts> list = new ArrayList<InfoOfContacts>();
        Cursor cursor = context.getContentResolver().query(contactUri,
                conColumn, null, null, null);

        while (cursor.moveToNext()) {
            InfoOfContacts info = new InfoOfContacts();
            long id = cursor.getLong(0);
            String name = cursor.getString(1);
            info.id = id;
            info.name = name;
            // 根据id,从数据表中查询这个人的电话信息
            Cursor phoneCursor = context.getContentResolver().query(dataUri,
                    dataColumn, "mimetype_id=5 and raw_contact_id=" + id, null,
                    null);
            if (phoneCursor.moveToNext()) {
                String phone = phoneCursor.getString(0);
                info.phone = phone;
            }
            phoneCursor.close();

            // 根据id,从数据表中查询这个人的邮箱信息
            Cursor emailCursor = context.getContentResolver().query(dataUri,
                    dataColumn, "mimetype_id=1 and raw_contact_id=" + id, null,
                    null);

            if (emailCursor.moveToNext()) {
                String email = emailCursor.getString(0);
                info.email = email;
            }
            emailCursor.close();
            if (info.phone == null && info.email == null)
                continue;
            list.add(info);
        }
        cursor.close();
        return list;
    }

    public static Cursor SelectName(Context context) {
		/* 查询姓名的id */
        Cursor cursor = context.getContentResolver().query(contactUri,
                conColumn, null, null, null);
        return cursor;
    }

    public static Cursor SelectPhone(Context context, long id) {
		/* 查询电话 */
        Cursor phoneCursor = context.getContentResolver().query(dataUri,
                dataColumn, "mimetype_id=5 and raw_contact_id=" + id, null,
                null);
        return phoneCursor;
    }

    public static Cursor SelectEmail(Context context, long id) {
		/* 查询电话 */
        Cursor emailCursor = context.getContentResolver().query(dataUri,
                dataColumn, "mimetype_id=1 and raw_contact_id=" + id, null,
                null);
        return emailCursor;
    }

    /**
     * 向通讯录添加时需要使用键值
     * <p/>
     * ContactsContract.RawContacts.CONTENT_URI 联系人表的Uri
     * <p/>
     * ContactsContract.Data.CONTENT_URI 数据表
     * <p/>
     * Data.RAW_CONTACT_ID 对应联系人表中的ID
     * <p/>
     * Data.MIMETYPE 数据类型
     * <p/>
     * StructuredName.GIVEN_NAME 姓名键
     * <p/>
     * Phone.NUMBER Email.DATA
     * <p/>
     * Phone.NUMBER
     */
	/*-----------------------------------向通讯录中插入数据------------------------------*/
	/* 插入的时候全部数据插入 */
    public static void Insert(String name, Context context, String phone,
                              String email) {
        // 向联系人表raw_contacts中添加新的联系人信息
        ContentValues value = new ContentValues();
        value.put("display_name", name);
        value.put("display_name_alt", name);

        // 返回新插入的记录Uri,Uri中包含了_id
        // content://com.android.contacts/raw_contacts/#8
        Uri datasUri = context.getContentResolver().insert(contactUri, value);
        // 根据Uri获取id
        long _id = ContentUris.parseId(datasUri);

        // 向数据表中插入联系人的姓名信息
        value.clear();
        value.put("raw_contact_id", _id);
        value.put("data1", name);
        value.put("mimetype", "vnd.android.cursor.item/name");

        context.getContentResolver().insert(dataUri, value);

        // 向数据表中插入联系人的电话信息
        value.put("data1", phone);
        value.put("mimetype", "vnd.android.cursor.item/phone_v2");

        context.getContentResolver().insert(dataUri, value);

        // 向数据表中插入联系人的邮箱信息
        value.put("data1", email);
        value.put("mimetype", "vnd.android.cursor.item/email_v2");

        context.getContentResolver().insert(dataUri, value);
    }

	/*-----------------------------------向通讯录中更新数据------------------------------*/

    /**
     * 通讯录可以重名，必须想办法获得ID才能删除
     */
    public static void Update(Context context, long id, String name,
                              String phone, String email) {

        ContentValues value = new ContentValues();
        value.put("display_name", name);
        value.put("display_name_alt", name);
        // 更新联系人信息
        context.getContentResolver().update(contactUri, value, "_id=" + id,
                null);

        // 更新数据表中联系人的姓名
        value.clear();
        value.put("data1", name);
        context.getContentResolver().update(dataUri, value,
                "mimetype_id=7 and raw_contact_id=" + id, null);

        // 更行数据表中联系人的电话
        value.put("data1", phone);
        context.getContentResolver().update(dataUri, value,
                "mimetype_id=5 and raw_contact_id=" + id, null);
        // 更行数据表中联系人的邮箱

        value.put("data1", email);
        context.getContentResolver().update(dataUri, value,
                "mimetype_id=1 and raw_contact_id=" + id, null);
    }

    public static void UpdateName(Context context, String name, long id) {
        ContentValues value = new ContentValues();
        value.put("display_name", name);
        value.put("display_name_alt", name);
        // 更新联系人信息
        context.getContentResolver().update(contactUri, value, "_id=" + id,
                null);

        // 更新数据表中联系人的姓名
        value.clear();
        value.put("data1", name);
        context.getContentResolver().update(dataUri, value,
                "mimetype_id=7 and raw_contact_id=" + id, null);
    }

    public static void UpdatePhone(Context context, String phone, long id) {
		/* 更新数据表中的姓名 */
        ContentValues values = new ContentValues();
        // 更行数据表中联系人的电话
        values.put("data1", phone);
        context.getContentResolver().update(dataUri, values,
                "mimetype_id=5 and raw_contact_id=" + id, null);
    }

    public static void UpdateEmail(Context context, String email, long id) {
		/* 更新数据表中的姓名 */
        ContentValues values = new ContentValues();
        values.put("data1", email);
        context.getContentResolver().update(dataUri, values,
                "mimetype_id=1 and raw_contact_id=" + id, null);
    }

	/*-----------------------------------向通讯录中删除数据------------------------------*/

    /**
     * 必须先删除数据表中的数据
     */
    public static void Delete(Context context, long id) {
        context.getContentResolver().delete(ContactsContract.Data.CONTENT_URI,
                ContactsContract.Data.RAW_CONTACT_ID + "=" + id, null);
        context.getContentResolver().delete(
                ContactsContract.Contacts.CONTENT_URI,
                ContactsContract.Contacts._ID + "=" + id, null);
        context.getContentResolver().delete(
                ContactsContract.RawContacts.CONTENT_URI,
                ContactsContract.RawContacts._ID + "=" + id, null);
    }

    static class InfoOfContacts {
        long id;
        String name;
        String phone;
        String email;

        public InfoOfContacts() {
            super();
        }

        public InfoOfContacts(long id, String name, String phone, String email) {
            super();
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.email = email;
        }

    }
}

