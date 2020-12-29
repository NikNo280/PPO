package com.example.battleship.Enum;


import com.example.battleship.R;

public enum TypeField {
    EMPTY(R.drawable.wrapper_green,0),
    SHIP(R.drawable.ic_launcher_background,1),
    HIT(R.drawable.common_google_signin_btn_icon_dark,2),
    LOSE(R.drawable.wrapper_aqua,3),
    DESTROY(R.drawable.square_red,4);

    private final int codeImage;
    private final int codeField;

    TypeField(int codeImage,int codeField) {
        this.codeImage = codeImage;
        this.codeField = codeField;
    }

    public int getCodeField() {
        return codeField;
    }
    public int getCodeImage() {
        return codeImage;
    }
}
