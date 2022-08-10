package com.example.mylogin;

public interface EditorView {

    void showProgress();
    void hideProgress();
    void onRequesstSuccess(String message);
    void onRequestError(String message);
}
