package Ultilities.component;

import GUI.ACC_Setting;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;

public class Header extends javax.swing.JPanel {

    public Header() {
        initComponents();
        setOpaque(false);
        //lb_icon.setIcon(new ImageIcon("E:\\HUFI\\Năm 3\\HK2\\Oracle\\DoAn\\QuanLyCafe\\src\\Ultilities\\icon\\coffee_bg.jpg"));
    }

    public void setUsername(String user){
        username.setText(user);
    }
    public void setLastLogin(String lastLogin){
        lb_lastLogin.setText(lastLogin);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        username = new javax.swing.JLabel();
        lb_lastLogin = new javax.swing.JLabel();
        settingAccount = new Ultilities.login.Background();
        background3 = new Ultilities.login.Background();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        username.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        username.setForeground(new java.awt.Color(0, 0, 0));
        username.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        username.setText("Administrator");
        username.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lb_lastLogin.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        lb_lastLogin.setForeground(new java.awt.Color(0, 0, 0));
        lb_lastLogin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_lastLogin.setText("Administrator");
        lb_lastLogin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        settingAccount.setImage("/Ultilities/icon/settings.png");
        settingAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                settingAccountMouseClicked(evt);
            }
        });

        background3.setImage("/Ultilities/icon/user.png");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(background3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(username, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_lastLogin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(settingAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(background3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(username)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_lastLogin))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(settingAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(454, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents
    ACC_Setting accInfo = null;
    private void settingAccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingAccountMouseClicked
        if(accInfo == null)
            accInfo = new ACC_Setting();
        accInfo.setVisible(true);
    }//GEN-LAST:event_settingAccountMouseClicked

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.fillRect(0, 0, 25, getHeight());
        g2.fillRect(getWidth() - 25, getHeight() - 25, getWidth(), getHeight());
        super.paintComponent(grphcs);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Ultilities.login.Background background3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lb_lastLogin;
    private Ultilities.login.Background settingAccount;
    private javax.swing.JLabel username;
    // End of variables declaration//GEN-END:variables
}
