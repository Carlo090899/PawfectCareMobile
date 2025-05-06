package com.example.pawfectcareapp.Common;

import android.content.Context;

import com.example.pawfectcareapp.Utils.FunctionInterface;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AlertsAndLoaders {

    public SweetAlertDialog showAlert(int id, String title, String message, Context context, FunctionInterface.Function myFunction) {
        SweetAlertDialog sDialog = null;
        switch (id) {
            case 0:
                sDialog = new SweetAlertDialog(context,
                        SweetAlertDialog.SUCCESS_TYPE);
                sDialog.setTitleText("GREAT!");
                sDialog.setContentText(message);
                sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        executeFunction(myFunction);
                    }
                });
                sDialog.setCancelable(false);
                sDialog.setCanceledOnTouchOutside(false);
                sDialog.show();
                break;
            case 1:
                sDialog = new SweetAlertDialog(context,
                        SweetAlertDialog.WARNING_TYPE);
                sDialog.setTitleText("Attention...");

                if ("".equals(message)) {
                    sDialog.setContentText("Something went wrong!");
                } else {
                    sDialog.setContentText(message);
                }

                sDialog.show();
                sDialog.setCancelable(false);
                sDialog.setCanceledOnTouchOutside(false);
                break;

            case 2:
                sDialog = new SweetAlertDialog(context,
                        SweetAlertDialog.WARNING_TYPE);
                sDialog.setTitleText("Attention...");
                sDialog.setContentText(message);
                sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
//                        executeFunction(myFunction);
                        sDialog.cancel();
                    }
                });
                sDialog.show();
                sDialog.setCancelable(false);
                sDialog.setCanceledOnTouchOutside(false);
                break;
            case 3:
                sDialog = new SweetAlertDialog(
                        context, SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText(message);
//                sDialog.getProgressHelper().setBarColor(context.getResources().getColor(cn.pedant.SweetAlert.R.color.blue_btn_bg_color));
                sDialog.show();
                sDialog.setCancelable(false);
                sDialog.setCanceledOnTouchOutside(false);
                break;

            case 4:
                sDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                sDialog.setTitleText(title);
                sDialog.setContentText(message);
                sDialog.setCancelText("No");
                sDialog.setConfirmText("Yes");
                sDialog.showCancelButton(true);
                sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                sDialog.cancel();

                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                executeFunction(myFunction);
                                sDialog.cancel();

                            }
                        })
                        .show();
                sDialog.setCanceledOnTouchOutside(false);
                break;

            case 5:
                sDialog = new SweetAlertDialog(context,
                        SweetAlertDialog.WARNING_TYPE);
                sDialog.setTitleText("");
                sDialog.setContentText(message);
                sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        executeFunction(myFunction);
                        sDialog.cancel();
                    }
                });
                sDialog.show();
                sDialog.setCanceledOnTouchOutside(false);
                break;

            case 6:
                sDialog = new SweetAlertDialog(context,
                        SweetAlertDialog.WARNING_TYPE);
                sDialog.setTitleText("Attention...");
                sDialog.setContentText(message);
                sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        executeFunction(myFunction);
                        sDialog.cancel();
                    }
                });
                sDialog.show();
                sDialog.setCanceledOnTouchOutside(false);
                break;

            case 7:
                sDialog = new SweetAlertDialog(context,
                        SweetAlertDialog.SUCCESS_TYPE);
                sDialog.setTitleText("GREAT!");
                sDialog.setContentText(message);
                sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                });
                sDialog.setCancelable(false);
                sDialog.setCanceledOnTouchOutside(false);
                sDialog.show();
                break;
        }

        return sDialog;


    }

    public void executeFunction(FunctionInterface.Function function) {
        function.perform();
    }


}
