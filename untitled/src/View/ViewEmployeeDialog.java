package View;

import Model.Employee;

import javax.swing.*;

public class ViewEmployeeDialog extends EditEmployeeForm {

    public ViewEmployeeDialog(Employee emp) {
        super(emp, null);
        setTitle("Xem thông tin nhân viên");
        disableAllFields();
        getBtnSave().setVisible(false);
        getBtnCancel().setText("Đóng");
    }

    private void disableAllFields() {
        getTxtName().setEditable(false);
        getCbGender().setEnabled(false);
        getCbDepartment().setEnabled(false);
        getCbPosition().setEnabled(false);
        getTxtStartDate().setEditable(false);
        getCbEmploymentType().setEnabled(false);
        getTxtBankName().setEditable(false);
        getTxtBankNumber().setEditable(false);
        getTxtBankBranch().setEditable(false);
    }
}
