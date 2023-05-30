/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI.ProfileUI;

import BLL.ExecuteData;
import BLL.GetData;
import GUI.SystemForm;
import javax.swing.JOptionPane;

/**
 *
 * @author phatl
 */
public class UpdateProfile extends javax.swing.JFrame {

    /**
     * Creates new form UpdateProfile
     */
    public UpdateProfile() {
        initComponents();
        loadCombobox();
    }

    public static String nameProfile = null;
    public SystemForm root = null;
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        comboSuggestionUI1 = new Ultilities.swing.Controls.ComboSuggestionUI();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_NameProfile = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        cb_resourceName = new Ultilities.swing.Controls.ComboBoxSuggestion();
        jPanel5 = new javax.swing.JPanel();
        cb_State = new Ultilities.swing.Controls.ComboBoxSuggestion();
        txt_num = new javax.swing.JTextField();
        btn_Luu = new Ultilities.swing.Controls.ButtonGradient();
        btn_Huy = new Ultilities.swing.Controls.ButtonGradient();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CẬP NHẬT PROFILE");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jPanel2.setLayout(new java.awt.GridLayout(2, 3, 10, 20));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("NAME PROFILE");
        jLabel2.setAlignmentX(0.5F);
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel2.add(jLabel2);
        jPanel2.add(txt_NameProfile);

        jPanel3.setLayout(new java.awt.GridLayout(1, 2));

        cb_resourceName.setToolTipText("Resource Name");
        jPanel3.add(cb_resourceName);

        jPanel2.add(jPanel3);

        jPanel5.setLayout(new java.awt.GridLayout(1, 2, 10, 20));

        cb_State.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_StateItemStateChanged(evt);
            }
        });
        jPanel5.add(cb_State);

        txt_num.setEditable(false);
        jPanel5.add(txt_num);

        jPanel2.add(jPanel5);

        btn_Luu.setText("Lưu");
        btn_Luu.setColor2(new java.awt.Color(51, 102, 255));
        btn_Luu.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_Luu.setSizeSpeed(5.0F);
        btn_Luu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LuuActionPerformed(evt);
            }
        });

        btn_Huy.setText("Hủy");
        btn_Huy.setColor1(new java.awt.Color(255, 0, 153));
        btn_Huy.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_Huy.setSizeSpeed(5.0F);
        btn_Huy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_HuyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(btn_Luu, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_Huy, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Luu, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(btn_Huy, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    String[] items = {"UNLIMITED","DEFAULT","n"};
    GetData x = new GetData();
    
    @SuppressWarnings("unchecked")
    private void loadCombobox(){
        for(var e : items){
            cb_State.addItem(e);
        }
        
        Object[][] temp = x.getAllResourceOfProfile();
        for(int i=0;i<temp.length;i++)
        {
            cb_resourceName.addItem(temp[i][0]);
        }
    }
    
    private void openEditTextBox(String state, javax.swing.JTextField txt){
        if(state.equalsIgnoreCase("n")){
            txt.setEditable(true);
        }else
            txt.setEditable(false);
    }
    
     private String getValueField( javax.swing.JTextField txt, javax.swing.JComboBox<String> cb){
        String valueCb = cb.getSelectedItem().toString();
        if(valueCb.equals("n"))
            return txt.getText();
        else
            return valueCb;
    }
    
    private void btn_HuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_HuyActionPerformed
        this.dispose();
    }//GEN-LAST:event_btn_HuyActionPerformed

    private void cb_StateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_StateItemStateChanged
        openEditTextBox(cb_State.getSelectedItem().toString(), txt_num);
    }//GEN-LAST:event_cb_StateItemStateChanged

    @SuppressWarnings("unchecked")
    private void btn_LuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LuuActionPerformed
        String resName = cb_resourceName.getSelectedItem().toString();
        String result = getValueField(txt_num, cb_State);
        
        if(!(resName.isEmpty() || result.isEmpty()))
        {
            if(ExecuteData.UpdateProfile(nameProfile, resName, result))
            {
                JOptionPane.showMessageDialog(this, "Cập nhật profile thành công !");
                root.loadDataProfile();
            }
            else
                JOptionPane.showMessageDialog(this, "Cập nhật profile thất bại !");
        
        }
    }//GEN-LAST:event_btn_LuuActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        txt_NameProfile.setText(nameProfile);
        txt_NameProfile.setEditable(false);
    }//GEN-LAST:event_formComponentShown

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Ultilities.swing.Controls.ButtonGradient btn_Huy;
    private Ultilities.swing.Controls.ButtonGradient btn_Luu;
    private Ultilities.swing.Controls.ComboBoxSuggestion cb_State;
    private Ultilities.swing.Controls.ComboBoxSuggestion cb_resourceName;
    private Ultilities.swing.Controls.ComboSuggestionUI comboSuggestionUI1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTextField txt_NameProfile;
    private javax.swing.JTextField txt_num;
    // End of variables declaration//GEN-END:variables
}
