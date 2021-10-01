package br.com.dio.app.repositories.ui

import android.database.Cursor
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cursoradapter.widget.SimpleCursorAdapter
import com.bumptech.glide.Glide


class UserViewBinder: SimpleCursorAdapter.ViewBinder{
        override fun setViewValue(view: View?, cursor: Cursor?, columnIndex: Int): Boolean {
            if (cursor != null) {
                if (columnIndex == cursor.getColumnIndex("login")) {
                    val v = view as TextView
                    v.text = cursor.getString(columnIndex)
                }
                if (columnIndex == cursor.getColumnIndex("avatar_url")) {
                    val myImageView:ImageView= view as ImageView

                    val url: String = cursor.getString(columnIndex)

                    Glide.with(myImageView.context).load(url).placeholder(android.R.drawable.progress_indeterminate_horizontal)
                        .into(myImageView)

                }
            }
            return true
        }

    }