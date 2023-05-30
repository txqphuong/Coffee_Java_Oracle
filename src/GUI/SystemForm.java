/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI;

import GUI.UserUI.EditUser;
import GUI.ProfileUI.UpdateProfile;
import GUI.ProfileUI.CreateProfile;
import BLL.ExecuteData;
import BLL.GetData;
import BLL.UpdateData;
import DTO.ImportExport;
import DTO.RWFile;
import GUI.UserUI.CreateUser;
import GUI.Role_Priv_Config.Privileges_;
import Ultilities.CMD.ExecuteCMD;
import Ultilities.ConvertData.ConvertDataORCL;
import Ultilities.swing.Controls.ButtonColumn;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.DefaultButtonModel.SELECTED;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import Ultilities.swing.AlertBox;

/**
 *
 * @author phatl
 */
public class SystemForm extends javax.swing.JPanel {

    /**
     * Creates new form SystemForm
     */
    private boolean flag = true;
    private GetData x = new GetData();
    private ExecuteData y = new ExecuteData();
    private UpdateData up = new UpdateData();
    ArrayList tableName = x.getTableName();
    ArrayList objectSchema=x.getObjectSchema();
    String statement="";
    
    
    @SuppressWarnings("unchecked")
    public SystemForm() {
        initComponents();        
        LoadComboBoxUserName();
    }

    public void showDataOnTable(Ultilities.swing.Controls.Table table,ArrayList arr)
    {
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        String[] columnNames = (String[])arr.get(0);
        String[][] data = ConvertDataORCL.ConvertObject2DToString2D((Object[][])arr.get(1));
        model.setDataVector(data, columnNames);
    }
   
    public void showDataOnTable_1(Ultilities.swing.Controls.Table table , ArrayList arr)
    {
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        String[] columnNames = (String[])arr.get(0);
        String[][] data = ConvertDataORCL.ConvertObject2DToString2D((Object[][])arr.get(1));
        model.setDataVector(data, columnNames);
        model.addColumn("");
        ButtonColumn btnCol = new ButtonColumn(table, kill, columnNames.length,"Kill");
        countRecord.setText("Số dòng: "+data.length);
    }
    
    
    //tab 2 - manager session
    Action kill = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
            //remove row
            JTable table = (JTable)e.getSource();
            int indexRow = Integer.valueOf( e.getActionCommand() );
            
            String SID = table.getValueAt(indexRow, 0).toString();
            String SerialID = table.getValueAt(indexRow, 1).toString();
            if(ExecuteData.ExecuteSql(SID, SerialID))
            {
                JOptionPane.showMessageDialog(table, "Kill session thành công !","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
                ((DefaultTableModel)table.getModel()).removeRow(indexRow);
            }
            else
            {
                JOptionPane.showMessageDialog(table, "Kill session thất bại !","Thông Báo",JOptionPane.ERROR_MESSAGE);
            }
        }
    };
    
    private void loadpriv()
    {
        try
        {
            for(Component com : Panel2.getComponents ())
            {
                    if(com instanceof JCheckBox)
                    {
                        JCheckBox box = (JCheckBox)com;
                        box.setSelected (false);
                    }
            }
            int row = tbl_role.getSelectedRow ();
            String tablename =cbo_objectname.getSelectedItem ().toString ();
            String role = tbl_role.getValueAt (row,0).toString ();
            ArrayList arr = x.getPrivRole (role,tablename);
            if(arr.size () ==0)
            {
                for(Component com : Panel2.getComponents ())
                {
                    if(com instanceof JCheckBox)
                    {
                        JCheckBox box = (JCheckBox)com; 
                        box.setSelected (false);
                    }
                }
            }
            else
            {
                for(int i = 0; i<arr.size ();i++)
                {
                    String priv = arr.get (i).toString ();
                        for(Component com : Panel2.getComponents ())
                    {
                        if(com instanceof JCheckBox)
                        {
                            JCheckBox box = (JCheckBox)com;

                            String ckbselect = box.getText ();
                            if(priv.compareTo (ckbselect) ==0)
                            {
                                box.setSelected (true);
                                break;
                            }
                        }
                    }
                }
            }
            
        
        }
        catch(Exception ex)
        {
                
        }
    }    
    
    private void LoadComboBoxObjectName()
    {
        for(var e:tableName)
        {
            cboObjectName.addItem(e.toString());
            cbo_objectname.addItem (e.toString ());
        }
    }
    
    
    //tab profile
    public void loadDataProfile(){
        //load combobox list name profile
        cb_ListProfile.removeAllItems();
        Object[][] nameProfiles = x.getAllProfileName();
        int n = nameProfiles.length;
        for(int i =0;i<n;i++)
        {
            cb_ListProfile.addItem(nameProfiles[i][0].toString());
        }
        lb_slProfile.setText("Số Lượng Profile: "+n);
    }
    
    
    public void loadUserData(){
        ArrayList temp = x.getAllUserAndProfile();
        DefaultTableModel model = (DefaultTableModel)table_User.getModel();
        String arr[][] = ConvertDataORCL.ConvertObject2DToString2D((Object[][])temp.get(1));
        model.setDataVector(arr, (Object[])temp.get(0));
        model.addColumn("");
        ButtonColumn btnCol = new ButtonColumn(table_User, EditUser, arr[0].length,"edit");
        lb_slUser.setText("Số Lượng User: "+arr.length);
    }
    
    EditUser editUserForm = null;
    Action EditUser = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
            //remove row
            JTable table = (JTable)e.getSource();
            int indexRow = Integer.valueOf( e.getActionCommand() );
            
            String user = table.getValueAt(indexRow, 1).toString();
            if(editUserForm == null)
                editUserForm = new EditUser();
            
            editUserForm.user = user;
            editUserForm.root = SystemForm.this;
            editUserForm.setVisible(true);
            
        }
    };
    
    private void loadCombobox_IETable(){
        ArrayList t = x.getTableName();
        cb_TableName_IE.removeAllItems();
        t.forEach((e) -> cb_TableName_IE.addItem(e));
        cb_TableName_IE.addItem("All");
    }
    
    // =========================
    // backup and restore
    // =========================
    
    private void loadTableDataFileOfBackupRestore(){
        DefaultTableModel model = (DefaultTableModel)table_dataFile.getModel();
        ArrayList temp = x.getAllDatafileForBackupInfo();
        Object[] header = (Object[])temp.get(0);
        Object[][] data = (Object[][])temp.get(1);
        model.setDataVector(data, header);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grRadio_Backup = new javax.swing.ButtonGroup();
        grRadio_Recover = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        TTHT = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table1 = new Ultilities.swing.Controls.Table();
        countRecord = new javax.swing.JLabel();
        buttonGradient1 = new Ultilities.swing.Controls.ButtonGradient();
        comboBoxSuggestion = new Ultilities.swing.Controls.ComboBoxSuggestion();
        SESSION = new javax.swing.JPanel();
        btnRefresh = new Ultilities.swing.Controls.ButtonGradient();
        btnDemo = new Ultilities.swing.Controls.ButtonGradient();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_tab2 = new Ultilities.swing.Controls.Table();
        numRecord_tab2 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        chkBox_blockingUser = new javax.swing.JCheckBox();
        AUDIT = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtPolicyName = new javax.swing.JTextField();
        cboObjectName = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        ckbInsert = new javax.swing.JCheckBox();
        ckbUpdate = new javax.swing.JCheckBox();
        ckbDelete = new javax.swing.JCheckBox();
        btnCreateAuditPolicy = new javax.swing.JButton();
        btn_showaudit = new javax.swing.JButton();
        btn_XemTT = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        cboObjectSchema = new javax.swing.JComboBox<>();
        QL_ROLE = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_role = new Ultilities.swing.Controls.Table();
        btn_Quyenle = new javax.swing.JButton();
        Panel2 = new javax.swing.JPanel();
        cbo_objectname = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        ckb_insert = new javax.swing.JCheckBox();
        ckb_delete = new javax.swing.JCheckBox();
        cbk_update = new javax.swing.JCheckBox();
        ckb_select = new javax.swing.JCheckBox();
        btn_InsertPriv = new javax.swing.JButton();
        btn_Revoke = new javax.swing.JButton();
        ckb_all = new javax.swing.JCheckBox();
        btn_Xoa = new javax.swing.JButton();
        btn_refresh = new javax.swing.JButton();
        Control_ROLE = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        lbl_Pass = new javax.swing.JLabel();
        txt_Pass = new javax.swing.JTextField();
        rdo_Pass = new javax.swing.JRadioButton();
        rdo_NotPass = new javax.swing.JRadioButton();
        txt_role_name = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btn_Create = new javax.swing.JButton();
        btn_updaterole = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        cbo_Username = new javax.swing.JComboBox<>();
        jScrollPane8 = new javax.swing.JScrollPane();
        tbl_userofrole = new Ultilities.swing.Controls.Table();
        btn_default = new javax.swing.JButton();
        rdo_default = new javax.swing.JRadioButton();
        btn_RevokeRoleToUser = new javax.swing.JButton();
        PROFILE = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        btn_TaoMoi = new Ultilities.swing.Controls.ButtonGradient();
        btn_CapNhat = new Ultilities.swing.Controls.ButtonGradient();
        btn_XoaProfile = new Ultilities.swing.Controls.ButtonGradient();
        jLabel9 = new javax.swing.JLabel();
        lb_slProfile = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        table_Profile = new Ultilities.swing.Controls.Table();
        lb_slProfile1 = new javax.swing.JLabel();
        cb_ListProfile = new Ultilities.swing.Controls.ComboBoxSuggestion();
        USER = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        btn_RemoveUser = new Ultilities.swing.Controls.ButtonGradient();
        lb_slUser = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        table_User = new Ultilities.swing.Controls.Table();
        lb_slProfile3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        btn_VPD = new javax.swing.JButton();
        btn_CreateUser = new Ultilities.swing.Controls.ButtonGradient();
        btn_grantSelectRole = new javax.swing.JButton();
        Backup_Restore = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        lb_slProfile5 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        rad_bkFull = new javax.swing.JRadioButton();
        rad_bkFullArchivelog = new javax.swing.JRadioButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        table_dataFile = new Ultilities.swing.Controls.Table();
        jScrollPane9 = new javax.swing.JScrollPane();
        cmdScreen = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        btn_LoadListBackup = new Ultilities.swing.Controls.buttonCustom();
        btn_showAllDataFileBKRS = new Ultilities.swing.Controls.buttonCustom();
        btnClearCMD = new Ultilities.swing.Controls.buttonCustom();
        chk_compressBK = new javax.swing.JCheckBox();
        txt_Date = new javax.swing.JFormattedTextField();
        txt_time = new javax.swing.JFormattedTextField();
        jLabel14 = new javax.swing.JLabel();
        rd_incre = new javax.swing.JRadioButton();
        rd_lv1 = new javax.swing.JRadioButton();
        rd_lv0 = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btn_backup = new Ultilities.swing.Controls.buttonCustom();
        btn_recovery = new Ultilities.swing.Controls.buttonCustom();
        AUDIT1 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtPolicyName1 = new javax.swing.JTextField();
        cboObjectName1 = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        ckbInsert1 = new javax.swing.JCheckBox();
        ckbUpdate1 = new javax.swing.JCheckBox();
        ckbDelete1 = new javax.swing.JCheckBox();
        btnCreateAuditPolicy1 = new javax.swing.JButton();
        btn_showaudit1 = new javax.swing.JButton();
        btn_XemTT1 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        cboObjectSchema1 = new javax.swing.JComboBox<>();
        ckbSelect = new javax.swing.JCheckBox();
        btnCreateStandardAudit = new javax.swing.JButton();
        btnEditAuditPolicy = new javax.swing.JButton();
        import_export = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        lb_slProfile4 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cb_TableName_IE = new Ultilities.swing.Controls.ComboBoxSuggestion();
        rad_sqlcl = new javax.swing.JRadioButton();
        rad_datapump = new javax.swing.JRadioButton();
        lb_IE = new javax.swing.JLabel();
        txt_pathFile = new javax.swing.JTextField();
        btn_browse = new Ultilities.swing.Controls.buttonCustom();
        cb_Dir = new Ultilities.swing.Controls.ComboBoxSuggestion();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        table_ImEx = new Ultilities.swing.Controls.Table();
        jPanel3 = new javax.swing.JPanel();
        btn_import = new Ultilities.swing.Controls.buttonCustom();
        btn_Export = new Ultilities.swing.Controls.buttonCustom();

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setToolTipText("");
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(1100, 700));
        jTabbedPane1.setName(""); // NOI18N
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1100, 700));
        jTabbedPane1.setVerifyInputWhenFocusTarget(false);

        TTHT.setBackground(new java.awt.Color(255, 255, 255));
        TTHT.setMinimumSize(new java.awt.Dimension(1100, 700));
        TTHT.setPreferredSize(new java.awt.Dimension(1100, 700));
        TTHT.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                TTHTComponentShown(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Hiển Thị Thông Tin");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(560, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 16, Short.MAX_VALUE))
        );

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(table1);

        countRecord.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        countRecord.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        countRecord.setText("jLabel2");
        countRecord.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        buttonGradient1.setText("Quản Lý Tablespace");
        buttonGradient1.setColor2(new java.awt.Color(0, 255, 153));
        buttonGradient1.setSizeSpeed(5.0F);
        buttonGradient1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGradient1ActionPerformed(evt);
            }
        });

        comboBoxSuggestion.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        comboBoxSuggestion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBoxSuggestionItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout TTHTLayout = new javax.swing.GroupLayout(TTHT);
        TTHT.setLayout(TTHTLayout);
        TTHTLayout.setHorizontalGroup(
            TTHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TTHTLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                .addComponent(comboBoxSuggestion, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(TTHTLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(buttonGradient1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(360, 360, 360)
                .addComponent(countRecord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );
        TTHTLayout.setVerticalGroup(
            TTHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TTHTLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(TTHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxSuggestion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(TTHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TTHTLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(buttonGradient1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(TTHTLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(countRecord, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(111, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("System Info", TTHT);

        SESSION.setBackground(new java.awt.Color(255, 255, 255));
        SESSION.setMinimumSize(new java.awt.Dimension(1100, 700));
        SESSION.setPreferredSize(new java.awt.Dimension(1100, 700));
        SESSION.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                SESSIONComponentShown(evt);
            }
        });

        btnRefresh.setText("Làm Mới");
        btnRefresh.setBorderPainted(false);
        btnRefresh.setColor2(new java.awt.Color(51, 51, 255));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setFocusable(false);
        btnRefresh.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnRefresh.setSizeSpeed(5.0F);
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnDemo.setText("Demo");
        btnDemo.setColor1(new java.awt.Color(153, 0, 255));
        btnDemo.setColor2(new java.awt.Color(255, 0, 255));
        btnDemo.setFocusPainted(false);
        btnDemo.setFocusable(false);
        btnDemo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDemo.setSizeSpeed(5.0F);
        btnDemo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDemoActionPerformed(evt);
            }
        });

        table_tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table_tab2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane2.setViewportView(table_tab2);

        numRecord_tab2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        numRecord_tab2.setText("jLabel2");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("QUẢN LÝ SESSION CỦA CÁC USER");

        chkBox_blockingUser.setBackground(new java.awt.Color(255, 255, 255));
        chkBox_blockingUser.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chkBox_blockingUser.setText("Blocking Session");
        chkBox_blockingUser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkBox_blockingUserItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout SESSIONLayout = new javax.swing.GroupLayout(SESSION);
        SESSION.setLayout(SESSIONLayout);
        SESSIONLayout.setHorizontalGroup(
            SESSIONLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1100, Short.MAX_VALUE)
            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SESSIONLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(numRecord_tab2, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(chkBox_blockingUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDemo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        SESSIONLayout.setVerticalGroup(
            SESSIONLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SESSIONLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(SESSIONLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDemo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkBox_blockingUser)
                    .addComponent(numRecord_tab2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 526, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("QL Session", SESSION);

        AUDIT.setBackground(new java.awt.Color(255, 255, 255));
        AUDIT.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                AUDITComponentShown(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Thiết lập Audit Policy");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Nhập tên Audit:");

        txtPolicyName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        cboObjectName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cboObjectName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboObjectNameItemStateChanged(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Chọn bảng cần Audit:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Kiểm toán các thao tác:");

        ckbInsert.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ckbInsert.setText("INSERT");

        ckbUpdate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ckbUpdate.setText("UPDATE");

        ckbDelete.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ckbDelete.setText("DELETE");

        btnCreateAuditPolicy.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCreateAuditPolicy.setText("Thiết lập");
        btnCreateAuditPolicy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateAuditPolicyActionPerformed(evt);
            }
        });

        btn_showaudit.setText("Audit");
        btn_showaudit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_showauditMouseClicked(evt);
            }
        });

        btn_XemTT.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_XemTT.setText("Xem thông tin");
        btn_XemTT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_XemTTMouseClicked(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("User");

        cboObjectSchema.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cboObjectSchema.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboObjectSchemaItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout AUDITLayout = new javax.swing.GroupLayout(AUDIT);
        AUDIT.setLayout(AUDITLayout);
        AUDITLayout.setHorizontalGroup(
            AUDITLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AUDITLayout.createSequentialGroup()
                .addGroup(AUDITLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AUDITLayout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(138, 138, 138)
                        .addGroup(AUDITLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ckbInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ckbDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ckbUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(AUDITLayout.createSequentialGroup()
                        .addGap(146, 146, 146)
                        .addGroup(AUDITLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel13))
                        .addGap(43, 43, 43)
                        .addGroup(AUDITLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cboObjectName, 0, 210, Short.MAX_VALUE)
                            .addComponent(txtPolicyName)
                            .addComponent(cboObjectSchema, 0, 210, Short.MAX_VALUE)))
                    .addGroup(AUDITLayout.createSequentialGroup()
                        .addGap(188, 188, 188)
                        .addComponent(btnCreateAuditPolicy, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51)
                        .addComponent(btn_showaudit, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(AUDITLayout.createSequentialGroup()
                        .addGap(178, 178, 178)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(btn_XemTT)))
                .addContainerGap(116, Short.MAX_VALUE))
        );
        AUDITLayout.setVerticalGroup(
            AUDITLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AUDITLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(AUDITLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(btn_XemTT))
                .addGap(21, 21, 21)
                .addGroup(AUDITLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboObjectSchema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(10, 10, 10)
                .addGroup(AUDITLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPolicyName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(AUDITLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AUDITLayout.createSequentialGroup()
                        .addGroup(AUDITLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cboObjectName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ckbInsert)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AUDITLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jLabel6)
                        .addGap(1, 1, 1)))
                .addComponent(ckbUpdate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ckbDelete)
                .addGap(32, 32, 32)
                .addGroup(AUDITLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_showaudit, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCreateAuditPolicy, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(206, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("QL Audit", AUDIT);

        QL_ROLE.setBackground(new java.awt.Color(255, 255, 255));
        QL_ROLE.setMinimumSize(new java.awt.Dimension(1100, 700));
        QL_ROLE.setPreferredSize(new java.awt.Dimension(1100, 700));
        QL_ROLE.setVerifyInputWhenFocusTarget(false);

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));

        tbl_role.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_role.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane3.setViewportView(tbl_role);

        btn_Quyenle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_Quyenle.setText("Cấp quyền lẻ");
        btn_Quyenle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_QuyenleActionPerformed(evt);
            }
        });

        Panel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thiệt lập quyền cho role", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        Panel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        cbo_objectname.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbo_objectname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_objectnameActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Đối tượng");

        ckb_insert.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ckb_insert.setText("INSERT");

        ckb_delete.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ckb_delete.setText("DELETE");

        cbk_update.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbk_update.setText("UPDATE");

        ckb_select.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ckb_select.setText("SELECT");

        btn_InsertPriv.setText("Cấp quyền");
        btn_InsertPriv.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_InsertPrivMousePressed(evt);
            }
        });

        btn_Revoke.setText("Thu hồi");
        btn_Revoke.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_RevokeMousePressed(evt);
            }
        });

        ckb_all.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ckb_all.setText("TẤT CẢ");

        javax.swing.GroupLayout Panel2Layout = new javax.swing.GroupLayout(Panel2);
        Panel2.setLayout(Panel2Layout);
        Panel2Layout.setHorizontalGroup(
            Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(Panel2Layout.createSequentialGroup()
                        .addComponent(ckb_insert)
                        .addGap(18, 18, 18)
                        .addComponent(ckb_delete)
                        .addGap(18, 18, 18)
                        .addComponent(cbk_update)
                        .addGap(18, 18, 18)
                        .addComponent(ckb_select)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ckb_all))
                    .addGroup(Panel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbo_objectname, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(72, 72, 72)
                .addComponent(btn_InsertPriv, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(btn_Revoke, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );
        Panel2Layout.setVerticalGroup(
            Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cbo_objectname, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckb_insert)
                    .addComponent(ckb_delete)
                    .addComponent(cbk_update)
                    .addComponent(ckb_select)
                    .addComponent(ckb_all)
                    .addComponent(btn_InsertPriv, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Revoke, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_Xoa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_Xoa.setText("Xóa role");
        btn_Xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XoaActionPerformed(evt);
            }
        });

        btn_refresh.setText("Refresh");
        btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_refreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(Panel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(btn_refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_Quyenle, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_Xoa, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jScrollPane3)
                        .addContainerGap())))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_Xoa, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                        .addComponent(btn_Quyenle, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                    .addComponent(btn_refresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Panel2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout QL_ROLELayout = new javax.swing.GroupLayout(QL_ROLE);
        QL_ROLE.setLayout(QL_ROLELayout);
        QL_ROLELayout.setHorizontalGroup(
            QL_ROLELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QL_ROLELayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        QL_ROLELayout.setVerticalGroup(
            QL_ROLELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QL_ROLELayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(188, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("QL Role", QL_ROLE);

        Control_ROLE.setBackground(new java.awt.Color(255, 255, 255));
        Control_ROLE.setMinimumSize(new java.awt.Dimension(1100, 700));
        Control_ROLE.setPreferredSize(new java.awt.Dimension(1100, 700));
        Control_ROLE.setVerifyInputWhenFocusTarget(false);
        Control_ROLE.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                Control_ROLEComponentShown(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thiệt lập role", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        jPanel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        lbl_Pass.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_Pass.setText("Mật khẩu");

        rdo_Pass.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(rdo_Pass);
        rdo_Pass.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rdo_Pass.setText("Mật khẩu");
        rdo_Pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdo_PassActionPerformed(evt);
            }
        });

        rdo_NotPass.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(rdo_NotPass);
        rdo_NotPass.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rdo_NotPass.setText("Không mật khẩu");
        rdo_NotPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdo_NotPassActionPerformed(evt);
            }
        });

        txt_role_name.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Tên role");

        btn_Create.setText("Tạo");
        btn_Create.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_CreateMouseClicked(evt);
            }
        });

        btn_updaterole.setText("Cập nhật");
        btn_updaterole.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_updateroleMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_Pass)
                    .addComponent(jLabel5))
                .addGap(41, 41, 41)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_Pass, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_role_name, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rdo_NotPass)
                    .addComponent(rdo_Pass, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 175, Short.MAX_VALUE)
                .addComponent(btn_Create, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(btn_updaterole, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txt_role_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdo_Pass))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_Pass, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_Pass)
                            .addComponent(rdo_NotPass)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Create, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_updaterole, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("THIẾT LẬP ROLES");

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("User");

        cbo_Username.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbo_Username.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                cbo_UsernameComponentShown(evt);
            }
        });
        cbo_Username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_UsernameActionPerformed(evt);
            }
        });

        tbl_userofrole.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane8.setViewportView(tbl_userofrole);

        btn_default.setText("Đặt mặc định");
        btn_default.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_defaultActionPerformed(evt);
            }
        });

        rdo_default.setText("Tất cả");

        btn_RevokeRoleToUser.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_RevokeRoleToUser.setText("Thu hồi");
        btn_RevokeRoleToUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_RevokeRoleToUserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(rdo_default, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(btn_default, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                            .addComponent(btn_RevokeRoleToUser, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15)
                            .addGap(36, 36, 36)
                            .addComponent(cbo_Username, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 1001, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(82, 82, 82))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbo_Username, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15))
                    .addComponent(btn_RevokeRoleToUser, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_default, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdo_default))
                .addContainerGap(219, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout Control_ROLELayout = new javax.swing.GroupLayout(Control_ROLE);
        Control_ROLE.setLayout(Control_ROLELayout);
        Control_ROLELayout.setHorizontalGroup(
            Control_ROLELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Control_ROLELayout.createSequentialGroup()
                .addGap(400, 400, 400)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(Control_ROLELayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Control_ROLELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Control_ROLELayout.setVerticalGroup(
            Control_ROLELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Control_ROLELayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Role", Control_ROLE);

        PROFILE.setBackground(new java.awt.Color(255, 255, 255));
        PROFILE.setMinimumSize(new java.awt.Dimension(1100, 700));
        PROFILE.setPreferredSize(new java.awt.Dimension(1100, 700));
        PROFILE.setVerifyInputWhenFocusTarget(false);
        PROFILE.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                PROFILEComponentShown(evt);
            }
        });

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setLayout(new java.awt.GridLayout(1, 3, 5, 0));

        btn_TaoMoi.setForeground(new java.awt.Color(0, 0, 0));
        btn_TaoMoi.setText("Tạo mới");
        btn_TaoMoi.setColor1(new java.awt.Color(51, 255, 204));
        btn_TaoMoi.setColor2(new java.awt.Color(0, 204, 204));
        btn_TaoMoi.setFocusPainted(false);
        btn_TaoMoi.setFocusable(false);
        btn_TaoMoi.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_TaoMoi.setSizeSpeed(5.0F);
        btn_TaoMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_TaoMoiActionPerformed(evt);
            }
        });
        jPanel11.add(btn_TaoMoi);

        btn_CapNhat.setForeground(new java.awt.Color(0, 0, 0));
        btn_CapNhat.setText("Cập nhật");
        btn_CapNhat.setColor1(new java.awt.Color(255, 255, 0));
        btn_CapNhat.setColor2(new java.awt.Color(255, 255, 153));
        btn_CapNhat.setFocusPainted(false);
        btn_CapNhat.setFocusable(false);
        btn_CapNhat.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_CapNhat.setSizeSpeed(5.0F);
        btn_CapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CapNhatActionPerformed(evt);
            }
        });
        jPanel11.add(btn_CapNhat);

        btn_XoaProfile.setText("Xóa");
        btn_XoaProfile.setColor1(new java.awt.Color(255, 0, 102));
        btn_XoaProfile.setColor2(new java.awt.Color(255, 102, 102));
        btn_XoaProfile.setFocusPainted(false);
        btn_XoaProfile.setFocusable(false);
        btn_XoaProfile.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_XoaProfile.setSizeSpeed(5.0F);
        btn_XoaProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XoaProfileActionPerformed(evt);
            }
        });
        jPanel11.add(btn_XoaProfile);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Thông Tin Profile");

        lb_slProfile.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lb_slProfile.setText("Số Lượng Profile:");

        table_Profile.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(table_Profile);

        lb_slProfile1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lb_slProfile1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_slProfile1.setText("QUẢN LÝ PROFILE");
        lb_slProfile1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        cb_ListProfile.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_ListProfileItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1100, Short.MAX_VALUE)
            .addComponent(lb_slProfile1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lb_slProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cb_ListProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(lb_slProfile1)
                .addGap(36, 36, 36)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cb_ListProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lb_slProfile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout PROFILELayout = new javax.swing.GroupLayout(PROFILE);
        PROFILE.setLayout(PROFILELayout);
        PROFILELayout.setHorizontalGroup(
            PROFILELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PROFILELayout.setVerticalGroup(
            PROFILELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PROFILELayout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 732, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jTabbedPane1.addTab("Profile", PROFILE);

        USER.setBackground(new java.awt.Color(255, 255, 255));
        USER.setMinimumSize(new java.awt.Dimension(1100, 700));
        USER.setPreferredSize(new java.awt.Dimension(1100, 700));
        USER.setVerifyInputWhenFocusTarget(false);
        USER.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                USERComponentShown(evt);
            }
        });

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jPanel13.setLayout(new java.awt.GridLayout(1, 3, 5, 0));

        btn_RemoveUser.setText("Xóa");
        btn_RemoveUser.setColor1(new java.awt.Color(255, 0, 102));
        btn_RemoveUser.setColor2(new java.awt.Color(255, 102, 102));
        btn_RemoveUser.setFocusPainted(false);
        btn_RemoveUser.setFocusable(false);
        btn_RemoveUser.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_RemoveUser.setSizeSpeed(5.0F);
        btn_RemoveUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_RemoveUserActionPerformed(evt);
            }
        });
        jPanel13.add(btn_RemoveUser);

        lb_slUser.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lb_slUser.setText("Số Lượng User:");

        table_User.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(table_User);

        lb_slProfile3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lb_slProfile3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_slProfile3.setText("QUẢN LÝ USER");
        lb_slProfile3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jButton1.setText("Xem role của user");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btn_VPD.setText("Test VPD");
        btn_VPD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_VPDActionPerformed(evt);
            }
        });

        btn_CreateUser.setForeground(new java.awt.Color(0, 0, 0));
        btn_CreateUser.setText("Tạo User");
        btn_CreateUser.setColor1(new java.awt.Color(51, 255, 204));
        btn_CreateUser.setColor2(new java.awt.Color(0, 204, 204));
        btn_CreateUser.setFocusPainted(false);
        btn_CreateUser.setFocusable(false);
        btn_CreateUser.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_CreateUser.setSizeSpeed(5.0F);
        btn_CreateUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CreateUserActionPerformed(evt);
            }
        });

        btn_grantSelectRole.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_grantSelectRole.setText("Cấp xem role cho user");
        btn_grantSelectRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_grantSelectRoleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addComponent(btn_CreateUser, javax.swing.GroupLayout.PREFERRED_SIZE, 528, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE))
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1100, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addComponent(lb_slProfile3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_slUser, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_VPD, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_grantSelectRole, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_slProfile3)
                    .addComponent(lb_slUser))
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_VPD, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_grantSelectRole, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_CreateUser, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout USERLayout = new javax.swing.GroupLayout(USER);
        USER.setLayout(USERLayout);
        USERLayout.setHorizontalGroup(
            USERLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        USERLayout.setVerticalGroup(
            USERLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(USERLayout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 129, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("User", USER);

        Backup_Restore.setBackground(new java.awt.Color(255, 255, 255));
        Backup_Restore.setMinimumSize(new java.awt.Dimension(1100, 700));
        Backup_Restore.setPreferredSize(new java.awt.Dimension(1100, 700));
        Backup_Restore.setVerifyInputWhenFocusTarget(false);
        Backup_Restore.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                Backup_RestoreComponentShown(evt);
            }
        });

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        lb_slProfile5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lb_slProfile5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_slProfile5.setText("BACKUP & RESTORE");
        lb_slProfile5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Chế Độ Backup");

        rad_bkFull.setBackground(new java.awt.Color(255, 255, 255));
        grRadio_Backup.add(rad_bkFull);
        rad_bkFull.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rad_bkFull.setText("FULL");

        rad_bkFullArchivelog.setBackground(new java.awt.Color(255, 255, 255));
        grRadio_Backup.add(rad_bkFullArchivelog);
        rad_bkFullArchivelog.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rad_bkFullArchivelog.setText("FULL WITH ARCHIVELOG");

        table_dataFile.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(table_dataFile);

        cmdScreen.setEditable(false);
        cmdScreen.setBackground(new java.awt.Color(0, 0, 0));
        cmdScreen.setColumns(20);
        cmdScreen.setForeground(new java.awt.Color(51, 255, 51));
        cmdScreen.setRows(5);
        cmdScreen.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        cmdScreen.setRequestFocusEnabled(false);
        jScrollPane9.setViewportView(cmdScreen);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Controls"));

        btn_LoadListBackup.setText("Load List Backup");
        btn_LoadListBackup.setBorderColor(new java.awt.Color(255, 255, 204));
        btn_LoadListBackup.setBorderPainted(false);
        btn_LoadListBackup.setColor(new java.awt.Color(255, 255, 153));
        btn_LoadListBackup.setColorClick(new java.awt.Color(255, 255, 204));
        btn_LoadListBackup.setColorOver(new java.awt.Color(255, 255, 204));
        btn_LoadListBackup.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_LoadListBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LoadListBackupActionPerformed(evt);
            }
        });

        btn_showAllDataFileBKRS.setText("Show All Datafile");
        btn_showAllDataFileBKRS.setBorderColor(new java.awt.Color(255, 255, 204));
        btn_showAllDataFileBKRS.setBorderPainted(false);
        btn_showAllDataFileBKRS.setColor(new java.awt.Color(255, 255, 153));
        btn_showAllDataFileBKRS.setColorClick(new java.awt.Color(255, 255, 204));
        btn_showAllDataFileBKRS.setColorOver(new java.awt.Color(255, 255, 204));
        btn_showAllDataFileBKRS.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_showAllDataFileBKRS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_showAllDataFileBKRSActionPerformed(evt);
            }
        });

        btnClearCMD.setForeground(new java.awt.Color(255, 255, 255));
        btnClearCMD.setText("Clear CMD");
        btnClearCMD.setBorderColor(new java.awt.Color(255, 153, 153));
        btnClearCMD.setBorderPainted(false);
        btnClearCMD.setColor(new java.awt.Color(255, 102, 102));
        btnClearCMD.setColorClick(new java.awt.Color(255, 153, 153));
        btnClearCMD.setColorOver(new java.awt.Color(255, 153, 153));
        btnClearCMD.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnClearCMD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearCMDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnClearCMD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_showAllDataFileBKRS, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .addComponent(btn_LoadListBackup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(btn_LoadListBackup, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_showAllDataFileBKRS, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnClearCMD, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        chk_compressBK.setBackground(new java.awt.Color(255, 255, 255));
        chk_compressBK.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chk_compressBK.setText("Compress");

        txt_Date.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));
        txt_Date.setText("01/01/2023");
        txt_Date.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txt_time.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("h:mm:ss a"))));
        txt_time.setText("00:00:00 PM");
        txt_time.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Time Recover");

        grRadio_Backup.add(rd_incre);
        rd_incre.setText("INCREMENT");

        grRadio_Recover.add(rd_lv1);
        rd_lv1.setText("LEVEL 1");

        grRadio_Recover.add(rd_lv0);
        rd_lv0.setText("LEVEL 0");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane7))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_slProfile5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel15Layout.createSequentialGroup()
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(rad_bkFull, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(rad_bkFullArchivelog, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(rd_incre))
                                    .addGroup(jPanel15Layout.createSequentialGroup()
                                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txt_Date, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txt_time, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(rd_lv0)
                                        .addGap(18, 18, 18)
                                        .addComponent(rd_lv1)
                                        .addGap(32, 32, 32)
                                        .addComponent(chk_compressBK)))
                                .addGap(18, 18, 18)))
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(lb_slProfile5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rad_bkFull)
                            .addComponent(rad_bkFullArchivelog)
                            .addComponent(rd_incre))
                        .addGap(12, 12, 12)
                        .addComponent(jLabel14)
                        .addGap(4, 4, 4)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(rd_lv1)
                                .addComponent(rd_lv0))
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(chk_compressBK)
                                .addComponent(txt_Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_time, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        btn_backup.setBackground(new java.awt.Color(102, 255, 153));
        btn_backup.setText("BACKUP");
        btn_backup.setBorderColor(new java.awt.Color(255, 255, 255));
        btn_backup.setColor(new java.awt.Color(102, 255, 153));
        btn_backup.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_backup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(129, 129, 129)
                .addComponent(btn_backup, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(459, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_backup, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        btn_recovery.setBackground(new java.awt.Color(102, 204, 255));
        btn_recovery.setText("RECOVERY");
        btn_recovery.setBorderColor(new java.awt.Color(102, 255, 255));
        btn_recovery.setColor(new java.awt.Color(102, 204, 255));
        btn_recovery.setColorClick(new java.awt.Color(153, 204, 255));
        btn_recovery.setColorOver(new java.awt.Color(204, 255, 255));
        btn_recovery.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_recovery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_recoveryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Backup_RestoreLayout = new javax.swing.GroupLayout(Backup_Restore);
        Backup_Restore.setLayout(Backup_RestoreLayout);
        Backup_RestoreLayout.setHorizontalGroup(
            Backup_RestoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Backup_RestoreLayout.createSequentialGroup()
                .addGroup(Backup_RestoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Backup_RestoreLayout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_recovery, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        Backup_RestoreLayout.setVerticalGroup(
            Backup_RestoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Backup_RestoreLayout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Backup_RestoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Backup_RestoreLayout.createSequentialGroup()
                        .addGroup(Backup_RestoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_recovery, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 161, Short.MAX_VALUE))))
        );

        jTabbedPane1.addTab("Backup/Restore", Backup_Restore);

        AUDIT1.setBackground(new java.awt.Color(255, 255, 255));
        AUDIT1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                AUDITComponentShown(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Thiết lập Audit Policy");
        jLabel16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        AUDIT1.add(jLabel16);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Nhập tên Audit:");
        AUDIT1.add(jLabel17);

        txtPolicyName1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AUDIT1.add(txtPolicyName1);

        cboObjectName1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cboObjectName1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboObjectNameItemStateChanged(evt);
            }
        });
        AUDIT1.add(cboObjectName1);

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setText("Chọn bảng cần Audit:");
        AUDIT1.add(jLabel18);

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Kiểm toán các thao tác:");
        AUDIT1.add(jLabel19);

        ckbInsert1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ckbInsert1.setText("INSERT");
        AUDIT1.add(ckbInsert1);

        ckbUpdate1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ckbUpdate1.setText("UPDATE");
        AUDIT1.add(ckbUpdate1);

        ckbDelete1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ckbDelete1.setText("DELETE");
        AUDIT1.add(ckbDelete1);

        btnCreateAuditPolicy1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCreateAuditPolicy1.setText("FGA");
        btnCreateAuditPolicy1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateAuditPolicyActionPerformed(evt);
            }
        });
        AUDIT1.add(btnCreateAuditPolicy1);

        btn_showaudit1.setText("Audit trail");
        btn_showaudit1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_showauditMouseClicked(evt);
            }
        });
        btn_showaudit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_showauditActionPerformed(evt);
            }
        });
        AUDIT1.add(btn_showaudit1);

        btn_XemTT1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_XemTT1.setText("Xem thông tin");
        btn_XemTT1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_XemTTMouseClicked(evt);
            }
        });
        AUDIT1.add(btn_XemTT1);

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setText("User");
        AUDIT1.add(jLabel20);

        cboObjectSchema1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cboObjectSchema1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboObjectSchemaItemStateChanged(evt);
            }
        });
        AUDIT1.add(cboObjectSchema1);

        ckbSelect.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ckbSelect.setText("SELECT");
        AUDIT1.add(ckbSelect);

        btnCreateStandardAudit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCreateStandardAudit.setText("Standard");
        btnCreateStandardAudit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateStandardAuditActionPerformed(evt);
            }
        });
        AUDIT1.add(btnCreateStandardAudit);

        btnEditAuditPolicy.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEditAuditPolicy.setText("Chỉnh sửa");
        btnEditAuditPolicy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditAuditPolicyActionPerformed(evt);
            }
        });
        AUDIT1.add(btnEditAuditPolicy);

        jTabbedPane1.addTab("QL Audit", AUDIT1);

        import_export.setBackground(new java.awt.Color(255, 255, 255));
        import_export.setMinimumSize(new java.awt.Dimension(1100, 700));
        import_export.setPreferredSize(new java.awt.Dimension(1100, 700));
        import_export.setVerifyInputWhenFocusTarget(false);
        import_export.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                import_exportComponentShown(evt);
            }
        });

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        lb_slProfile4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lb_slProfile4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_slProfile4.setText("IMPORT AND EXPORT DATA");
        lb_slProfile4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setText("Bảng Dữ Liệu");

        cb_TableName_IE.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_TableName_IEItemStateChanged(evt);
            }
        });

        rad_sqlcl.setBackground(new java.awt.Color(255, 255, 255));
        grRadio_Backup.add(rad_sqlcl);
        rad_sqlcl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rad_sqlcl.setText("SQLCL");

        rad_datapump.setBackground(new java.awt.Color(255, 255, 255));
        grRadio_Backup.add(rad_datapump);
        rad_datapump.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rad_datapump.setText("DATAPUMP");
        rad_datapump.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rad_datapumpItemStateChanged(evt);
            }
        });

        lb_IE.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_IE.setText("File Export");

        txt_pathFile.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btn_browse.setBackground(new java.awt.Color(102, 153, 255));
        btn_browse.setText("browse");
        btn_browse.setBorderColor(new java.awt.Color(102, 153, 255));
        btn_browse.setColor(new java.awt.Color(102, 153, 255));
        btn_browse.setColorClick(new java.awt.Color(204, 204, 255));
        btn_browse.setColorOver(new java.awt.Color(102, 153, 255));
        btn_browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_browseActionPerformed(evt);
            }
        });

        cb_Dir.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_DirItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lb_slProfile4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_TableName_IE, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(lb_IE, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_Dir, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 324, Short.MAX_VALUE)
                        .addComponent(txt_pathFile, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(rad_datapump, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rad_sqlcl, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_browse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(lb_slProfile4, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cb_TableName_IE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cb_Dir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lb_IE, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_pathFile, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn_browse, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(rad_sqlcl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rad_datapump)))
                .addGap(0, 2, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        table_ImEx.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(table_ImEx);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        btn_import.setBackground(new java.awt.Color(102, 255, 153));
        btn_import.setText("IMPORT");
        btn_import.setBorderColor(new java.awt.Color(255, 255, 255));
        btn_import.setColor(new java.awt.Color(102, 255, 153));
        btn_import.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_import.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_importActionPerformed(evt);
            }
        });

        btn_Export.setBackground(new java.awt.Color(102, 204, 255));
        btn_Export.setText("EXPORT");
        btn_Export.setBorderColor(new java.awt.Color(102, 255, 255));
        btn_Export.setColor(new java.awt.Color(102, 204, 255));
        btn_Export.setColorClick(new java.awt.Color(153, 204, 255));
        btn_Export.setColorOver(new java.awt.Color(204, 255, 255));
        btn_Export.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_Export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ExportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(129, 129, 129)
                .addComponent(btn_import, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Export, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(92, 92, 92))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btn_import, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btn_Export, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 7, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout import_exportLayout = new javax.swing.GroupLayout(import_export);
        import_export.setLayout(import_exportLayout);
        import_exportLayout.setHorizontalGroup(
            import_exportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, import_exportLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        import_exportLayout.setVerticalGroup(
            import_exportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(import_exportLayout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Import/Export", import_export);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 763, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_updateroleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_updateroleMousePressed
        // TODO add your handling code here:
        String name = txt_role_name.getText ();
        String pass = txt_Pass.getText ();
       
        if(rdo_Pass.isSelected ())
        {
             if(pass.isBlank() || pass.isEmpty()|| name.isBlank() || name.isEmpty())
             {
                JOptionPane.showMessageDialog(this, "Bạn chưa nhập mật khẩu hoặc tên vui lòng kiểm tra lại!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
                 return;
             }
            up.alterRoleWithPass (name,pass);
            JOptionPane.showMessageDialog(this, "Cập nhật role thành công!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
            showDataOnTable(tbl_role,x.getDataRole ());
        }
        else
        {
            if(rdo_NotPass.isSelected ())
            {
                if(name.isBlank() || name.isEmpty())
                {
                   JOptionPane.showMessageDialog(this, "Bạn chưa nhập tên role!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                up.alterRoleWithNotPass (name);
                JOptionPane.showMessageDialog(this, "Cập nhật role thành công!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
                showDataOnTable(tbl_role,x.getDataRole ());
            }
        }
    }//GEN-LAST:event_btn_updateroleMousePressed

    private void btn_CreateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_CreateMouseClicked
        // TODO add your handling code here:
        String name = txt_role_name.getText ();
        String pass = txt_Pass.getText ();
        if(rdo_Pass.isSelected ())
        {
            up.createRoleWithPass (name,pass);
            JOptionPane.showMessageDialog(this, "Tạo role thành công!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);

        }
        else
        {
            up.createRoleWithNotPass (name);
            JOptionPane.showMessageDialog(this, "Tạo role thành công!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
            tbl_role.updateUI ();
        }
    }//GEN-LAST:event_btn_CreateMouseClicked

    private void rdo_NotPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdo_NotPassActionPerformed
        // TODO add your handling code here:
        if(rdo_NotPass.isSelected ())
        {
            flag = false;
            lbl_Pass.setVisible (flag);
            txt_Pass.setVisible (flag);
        }
        else
        {
            flag = true;
            lbl_Pass.setVisible (flag);
            txt_Pass.setVisible (flag);
        }
    }//GEN-LAST:event_rdo_NotPassActionPerformed

    private void rdo_PassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdo_PassActionPerformed
        // TODO add your handling code here:
        if(rdo_Pass.isSelected ())
        {
            flag = true;
            lbl_Pass.setVisible (flag);
            txt_Pass.setVisible (flag);
        }
        else
        {
            flag = false;
            lbl_Pass.setVisible (flag);
            txt_Pass.setVisible (flag);
        }
    }//GEN-LAST:event_rdo_PassActionPerformed

    private void btn_RevokeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_RevokeMousePressed
        // TODO add your handling code here:
        String priv = "";
        int row = tbl_role.getSelectedRow ();
        String name = tbl_role.getValueAt (row,0).toString ();
        String obj = cbo_objectname.getSelectedItem ().toString ();
        if(ckb_all.isSelected ())
        {
            up.revokeAllToRole (name,obj);

        }
        else
        {
            for(Component com : Panel2.getComponents ())
            {
                if(com instanceof JCheckBox)
                {
                    JCheckBox box = (JCheckBox)com;
                    if(box.isSelected ())
                    {
                        priv += box.getText () + ",";
                    }
                }
            }
            try {
                up.revokeRole (priv.substring (0,priv.length ()-1),name,obj);
                JOptionPane.showMessageDialog(this, String.format ("Thu hồi quyền cho %s thành công!", name),"Thông Báo",JOptionPane.INFORMATION_MESSAGE);
                loadpriv ();
            } catch ( Exception e ) {
            }
        }

    }//GEN-LAST:event_btn_RevokeMousePressed

    private void btn_InsertPrivMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_InsertPrivMousePressed
        // TODO add your handling code here:
        String priv = "";
        int row = tbl_role.getSelectedRow ();
        String name = tbl_role.getValueAt (row,0).toString ();
        String obj = cbo_objectname.getSelectedItem ().toString ();
        if(ckb_all.isSelected ())
        {
            
            if(up.grantAllToRole (name,obj))
            {
                JOptionPane.showMessageDialog(this, String.format ("Cấp quyền cho %s thành công!", name),"Thông Báo",JOptionPane.INFORMATION_MESSAGE);
                loadpriv ();
            }
            else
            {
                JOptionPane.showMessageDialog(this, String.format ("Cấp quyền cho %s thất bại!", name),"Thông Báo",JOptionPane.INFORMATION_MESSAGE);
            }

        }
        else
        {
            for(Component com : Panel2.getComponents ())
            {
                if(com instanceof JCheckBox)
                {
                    JCheckBox box = (JCheckBox)com;
                    if(box.isSelected ())
                    {
                        priv += box.getText ()+ ",";
                    }
                }
            }
            try
            {
                if(up.grantPrivToRole (priv.substring (0,priv.length ()-1),name,obj))
                {
                    JOptionPane.showMessageDialog(this, String.format ("Cấp quyền cho %s thành công!", name),"Thông Báo",JOptionPane.INFORMATION_MESSAGE);
                    loadpriv ();
                }
                else
                {
                    JOptionPane.showMessageDialog(this, String.format ("Cấp quyền cho %s thất bại!", name),"Thông Báo",JOptionPane.INFORMATION_MESSAGE);     
               }
                
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(this, String.format ("Quyền %s có thể đã được cấp! Vui lòng kiểm tra lại!", name),"Thông Báo",JOptionPane.INFORMATION_MESSAGE);
            }

        }

    }//GEN-LAST:event_btn_InsertPrivMousePressed

    private void cbo_objectnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_objectnameActionPerformed
        // TODO add your handling code here:
        loadpriv ();
    }//GEN-LAST:event_cbo_objectnameActionPerformed

    private void btnDemoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDemoActionPerformed
        Demo_KillSession t = new Demo_KillSession();
        t.setVisible(true);
    }//GEN-LAST:event_btnDemoActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        showDataOnTable_1(table_tab2, x.showSessionCurrent());
        numRecord_tab2.setText("Số lượng Session: "+table_tab2.getRowCount());
        chkBox_blockingUser.setSelected(false);
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void buttonGradient1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonGradient1ActionPerformed
        S_Tablespaces t = new S_Tablespaces();
        t.setVisible(true);
    }//GEN-LAST:event_buttonGradient1ActionPerformed

    private void comboBoxSuggestionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBoxSuggestionItemStateChanged
        String value = evt.getItem().toString();
        switch(value){
            case "SGA":
            showDataOnTable(table1,x.showSGA());
            break;
            case "PGA":
            showDataOnTable(table1,x.showPGA());
            break;
            case "ControlFile":
            showDataOnTable(table1,x.showControlFile());
            break;
            case "SPFile":
            showDataOnTable(table1,x.showSPFile());
            break;
            case "Instance":
            showDataOnTable(table1,x.showInfoInstance());
            break;
            case "DataFile":
            showDataOnTable(table1,x.showDataFile());
            break;
            case "Process":
            showDataOnTable(table1,x.showProcess());
            break;
            case "SessionProcess":
            showDataOnTable(table1,x.showProcessOfSession());
            break;
            case "Policy Is Valid":
            showDataOnTable(table1,x.getAllPoliciesInDB());
            break;
            case "Database":
            showDataOnTable(table1,x.showInfoDatabase());
            break;
        }
    }//GEN-LAST:event_comboBoxSuggestionItemStateChanged
    
    CreateProfile fCrePro = null;
    private void SESSIONComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_SESSIONComponentShown
        showDataOnTable_1(table_tab2, x.showSessionCurrent());
        numRecord_tab2.setText("Số lượng Session: "+table_tab2.getRowCount());
    }//GEN-LAST:event_SESSIONComponentShown

    private void PROFILEComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_PROFILEComponentShown
        loadDataProfile();
    }//GEN-LAST:event_PROFILEComponentShown
    
    CreateUser fCreUser = null;
    private void btn_CreateUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CreateUserActionPerformed
        if(fCreUser == null)
            fCreUser = new CreateUser();
        fCreUser.setVisible(true);
        fCreUser.root = this;
    }//GEN-LAST:event_btn_CreateUserActionPerformed

    private void btn_RemoveUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_RemoveUserActionPerformed
        int indexRow = table_User.getSelectedRow();
        String user = table_User.getValueAt(indexRow, 1).toString();
        if(JOptionPane.showConfirmDialog(this, "Bạn chắc chắn xóa User: "+user) == JOptionPane.YES_OPTION)
            if(ExecuteData.DropUser(user)){
                JOptionPane.showMessageDialog(this, "Xóa Profile '"+ user +"' thành công !");
                loadUserData();
            }
            else
                JOptionPane.showMessageDialog(this, "Xóa User '"+ user +"' thất bại !");
    }//GEN-LAST:event_btn_RemoveUserActionPerformed

    private void USERComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_USERComponentShown
        loadUserData();
    }//GEN-LAST:event_USERComponentShown

    Object[][] AllDir= x.getAllDirectory(); 
    private void import_exportComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_import_exportComponentShown
        loadCombobox_IETable();
        rad_sqlcl.setSelected(true);
        cb_Dir.setEnabled(false);
    
        cb_Dir.removeAllItems();
        for(int i = 0;i<AllDir.length;i++)
        {
            cb_Dir.addItem(AllDir[i][1].toString());
        }
    }//GEN-LAST:event_import_exportComponentShown
   
    private void btn_ExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ExportActionPerformed
        String tableName = cb_TableName_IE.getSelectedItem().toString();
        
        if(tableName != "All"){
            if(rad_datapump.isSelected() == true)
            {
                String path = cb_Dir.getSelectedItem().toString();
                if(ImportExport.ExportDataPump("expdp datacaphe/datacaphe tables="+tableName+" directory="+path+" dumpfile = exp_data_"+tableName+".dmp logfile= exp_data"+tableName+".log"))
                {
                    JOptionPane.showMessageDialog(this, "Export Datapump hoàn thành !");
                }
                else
                    JOptionPane.showMessageDialog(this, "Export Datapump thất bại !");
            }
            else
            {
                String path = txt_pathFile.getText();
                if(ImportExport.ExportDataSqlcl(path, tableName))
                {
                    JOptionPane.showMessageDialog(this, "Export hoàn thành ! ("+path+")");
                }
                else
                    JOptionPane.showMessageDialog(this, "Export thất bại !");
                    
            }
        }
        else
        {
            if(rad_datapump.isSelected() == true)
            {
                String path = cb_Dir.getSelectedItem().toString();
                if(ImportExport.ExportDataPump("expdp datacaphe/datacaphe full=y directory="+path+" dumpfile = exp_data_"+tableName+".dmp logfile= exp_data.log"))
                {
                    JOptionPane.showMessageDialog(this, "Export Datapump hoàn thành !");
                }
                else
                    JOptionPane.showMessageDialog(this, "Export Datapump thất bại !");
            }
        }
    }//GEN-LAST:event_btn_ExportActionPerformed

    private void btn_browseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_browseActionPerformed

        JFileChooser openFileChooser = new JFileChooser(); 
        //openFileChooser.setCurrentDirectory (new File(txt_pathFile.getText () + ":\\"));
        openFileChooser.setFileFilter (new FileNameExtensionFilter("",".csv"));
        int returnvalue = openFileChooser.showOpenDialog(this);
        if(returnvalue == JFileChooser.APPROVE_OPTION)
        {
            File file = openFileChooser.getSelectedFile ();
            String absolutePath = file.getAbsolutePath();
            txt_pathFile.setText(absolutePath);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Bạn chưa chọn đường dẫn","Lỗi tạo",JOptionPane.ERROR_MESSAGE);

        }
        
    }//GEN-LAST:event_btn_browseActionPerformed

    private void cb_TableName_IEItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_TableName_IEItemStateChanged
        table_ImEx.removeAll();
        String tableName = evt.getItem().toString();
        ArrayList temp = x.getDataTableByName(tableName);
        DefaultTableModel model = (DefaultTableModel)table_ImEx.getModel();
        model.setDataVector((Object[][])temp.get(1), (Object[])temp.get(0));
    }//GEN-LAST:event_cb_TableName_IEItemStateChanged

    private void rad_datapumpItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rad_datapumpItemStateChanged
        if(evt.getStateChange() == SELECTED)
        {
            lb_IE.setText("File Export");
            cb_Dir.setEnabled(false);
            btn_browse.setVisible(true);
            txt_pathFile.setText("");
        }
        else
        {   
            btn_browse.setVisible(false);
            lb_IE.setText("Dir Export");
            cb_Dir.setEnabled(true);
            txt_pathFile.setText("");
        }
    }//GEN-LAST:event_rad_datapumpItemStateChanged

    private void cb_DirItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_DirItemStateChanged
        int index=cb_Dir.getSelectedIndex();
        txt_pathFile.setText(AllDir[index][2].toString());
    }//GEN-LAST:event_cb_DirItemStateChanged

    private void btn_importActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_importActionPerformed
       
        if(rad_datapump.isSelected() == true)
            {
                 String tableName = cb_TableName_IE.getSelectedItem().toString();
                String path = cb_Dir.getSelectedItem().toString();
                if(ImportExport.ExportDataPump("impdp datacaphe/datacaphe tables="+tableName+" directory="+path+" dumpfile = exp_data_"+tableName+".dmp logfile= exp_data_"+tableName+".log TABLE_EXISTS_ACTION=APPEND"))
                {
                    JOptionPane.showMessageDialog(this, "Import Datapump hoàn thành !");
                }
                else
                    JOptionPane.showMessageDialog(this, "Import Datapump thất bại !");
            }
            else//sqlcl
            {
             String tableName = cb_TableName_IE.getSelectedItem().toString();
                String path = txt_pathFile.getText();
                if(ImportExport.ImportDataSqlcl(path, tableName))
                {
                    JOptionPane.showMessageDialog(this, "Import xong ! ("+path+")");
                }
                else
                    JOptionPane.showMessageDialog(this, "Export thất bại !");
                    
            }
    }//GEN-LAST:event_btn_importActionPerformed

    private void Backup_RestoreComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_Backup_RestoreComponentShown
        rad_bkFull.setSelected(true);
        
        loadTableDataFileOfBackupRestore();
    }//GEN-LAST:event_Backup_RestoreComponentShown
    
    @SuppressWarnings("unchecked")
    private void TTHTComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_TTHTComponentShown
        comboBoxSuggestion.addItem("SGA");
        comboBoxSuggestion.addItem("PGA");
        comboBoxSuggestion.addItem("Process");
        comboBoxSuggestion.addItem("ControlFile");
        comboBoxSuggestion.addItem("Instance");
        comboBoxSuggestion.addItem("SessionProcess");
        comboBoxSuggestion.addItem("SPFile");
        comboBoxSuggestion.addItem("DataFile");
        comboBoxSuggestion.addItem("Database");
        comboBoxSuggestion.addItem("Policy Is Valid");

        showDataOnTable(table1,x.showSGA());
        LoadComboBoxObjectName();
        showDataOnTable(tbl_role,x.getDataRole ());
        flag = false;
        lbl_Pass.setVisible (flag);
        txt_Pass.setVisible (flag);
        cbo_objectname.setSelectedIndex (0);
    }//GEN-LAST:event_TTHTComponentShown

    private void btn_backupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backupActionPerformed

        //chức năng nén file backup
        if(chk_compressBK.isSelected())
        {
            if(rad_bkFull.isSelected()){
                File file = new File("src/ResourceSQL/backupFull_CP.txt");
                if(file.canRead())
                {
                    System.out.println(file.getAbsoluteFile().toString());
                    ExecuteCMD.txtAreas = cmdScreen; 
                    if(ExecuteCMD.runCommand("RMAN @"+file.getAbsolutePath(), true)){
                        JOptionPane.showMessageDialog(null, "Backup Database Done !","Backup Alert",JOptionPane.INFORMATION_MESSAGE);
                    }
                    else
                        JOptionPane.showMessageDialog(null, "Backup Database Has Error !","Backup Alert",JOptionPane.ERROR_MESSAGE);
                } 
            }
            else if(rad_bkFullArchivelog.isSelected()){
                File file = new File("src/ResourceSQL/backupFullArchivelog_CP.txt");
                if(file.canRead())
                {
                    System.out.println(file.getAbsoluteFile().toString());
                    ExecuteCMD.txtAreas = cmdScreen; 
                    if(ExecuteCMD.runCommand("RMAN @"+file.getAbsolutePath(), true)){
                        JOptionPane.showMessageDialog(null, "Backup Database Done !","Backup Alert",JOptionPane.INFORMATION_MESSAGE);
                    }
                    else
                        JOptionPane.showMessageDialog(null, "Backup Database Has Error !","Backup Alert",JOptionPane.ERROR_MESSAGE);
                } 
            }
        }
        //không dùng nén file
        else{
            if(rad_bkFull.isSelected()){
                File file = new File("src/ResourceSQL/backupFull.txt");
                if(file.canRead())
                {
                    System.out.println(file.getAbsoluteFile().toString());
                    ExecuteCMD.txtAreas = cmdScreen; 
                    if(ExecuteCMD.runCommand("RMAN @"+file.getAbsolutePath(), true)){
                        JOptionPane.showMessageDialog(null, "Backup Database Done !","Backup Alert",JOptionPane.INFORMATION_MESSAGE);
                    }
                    else
                        JOptionPane.showMessageDialog(null, "Backup Database Has Error !","Backup Alert",JOptionPane.ERROR_MESSAGE);
                } 
            }
            else if(rad_bkFullArchivelog.isSelected()){
                File file = new File("src/ResourceSQL/backupFullArchivelog.txt");
                if(file.canRead())
                {
                    System.out.println(file.getAbsoluteFile().toString());
                    ExecuteCMD.txtAreas = cmdScreen; 
                    if(ExecuteCMD.runCommand("RMAN @"+file.getAbsolutePath(), true)){
                        JOptionPane.showMessageDialog(null, "Backup Database Done !","Backup Alert",JOptionPane.INFORMATION_MESSAGE);
                    }
                    else
                        JOptionPane.showMessageDialog(null, "Backup Database Has Error !","Backup Alert",JOptionPane.ERROR_MESSAGE);
                } 
            // backup increment
            }else if(rd_incre.isSelected()){
                //choose level 
                File file = null;
                //level 0
                if(rd_lv0.isSelected()){
                    file = new File("src/ResourceSQL/Incre0.txt");
                }
                //level 1
                else if(rd_lv1.isSelected()){
                    file = new File("src/ResourceSQL/Incre1.txt");
                }
                //backup increment
                if(file.canRead())
                {
                    System.out.println(file.getAbsoluteFile().toString());
                    ExecuteCMD.txtAreas = cmdScreen; 
                    if(ExecuteCMD.runCommand("RMAN @"+file.getAbsolutePath(), true)){
                        JOptionPane.showMessageDialog(null, "Backup Database Done !","Backup Alert",JOptionPane.INFORMATION_MESSAGE);
                    }
                    else
                        JOptionPane.showMessageDialog(null, "Backup Database Has Error !","Backup Alert",JOptionPane.ERROR_MESSAGE);
                } 
            }
        }
           
    }//GEN-LAST:event_btn_backupActionPerformed

    private void btn_showAllDataFileBKRSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_showAllDataFileBKRSActionPerformed
        loadTableDataFileOfBackupRestore();
    }//GEN-LAST:event_btn_showAllDataFileBKRSActionPerformed

    private void btn_recoveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_recoveryActionPerformed
        File file = new File("src/ResourceSQL/recovery.txt");
       
        String content = String.format("connect target \"sys/Oracle123 as sysdba\";"
                +"\nSTARTUP FORCE MOUNT;"
                +"\nrun{"
                +"\nset UNTIL TIME \"to_date('%s %s','dd/mm/yyyy hh:mi:ss am')\";"
                +"\nrestore database;"
                +"\nrecover database;"
                +"\nalter database open noresetlogs;"
                +"\n}", txt_Date.getText(), txt_time.getText());
        //ghi file
        RWFile.write(file.getAbsolutePath(), content);
        //execute file
        if(file.canRead())
        {
            System.out.println(file.getAbsoluteFile().toString());
            ExecuteCMD.txtAreas = cmdScreen; 
            if(ExecuteCMD.runCommand("RMAN @"+file.getAbsolutePath(), true)){
                JOptionPane.showMessageDialog(null, "Recover Database Done !","Recover Alert",JOptionPane.INFORMATION_MESSAGE);
            }
            else
                JOptionPane.showMessageDialog(null, "Recover Database Has Error !","Recover Alert",JOptionPane.ERROR_MESSAGE);
        } 
    }//GEN-LAST:event_btn_recoveryActionPerformed

    private void btnClearCMDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearCMDActionPerformed
        cmdScreen.setText("");
    }//GEN-LAST:event_btnClearCMDActionPerformed

    private void btn_LoadListBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LoadListBackupActionPerformed
        File file = new File("src/ResourceSQL/showListBackup.txt");
        if(file.canRead())
        {
            System.out.println(file.getAbsoluteFile().toString());
            ExecuteCMD.txtAreas = cmdScreen; 
            if(ExecuteCMD.runCommand("RMAN @"+file.getAbsolutePath(), true)){
                JOptionPane.showMessageDialog(null, "Run Done !","Run Command Alert",JOptionPane.INFORMATION_MESSAGE);
            }
            else
                JOptionPane.showMessageDialog(null, "Run Has Error !","Run Command Alert",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_LoadListBackupActionPerformed

    private void chkBox_blockingUserItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkBox_blockingUserItemStateChanged
        showDataOnTable_1(table_tab2, x.showSessionCurrentBlocking());
        numRecord_tab2.setText("Số lượng Session: "+table_tab2.getRowCount());        
    }//GEN-LAST:event_chkBox_blockingUserItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int row = table_User.getSelectedRow();
        if(row  >= 0)
        {
            Role_User t = new Role_User(table_User.getValueAt(row, 1).toString());
            t.setVisible(true);
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Chưa chọn dòng","Lỗi",JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btn_XoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XoaActionPerformed
        // TODO add your handling code here:
        int row = tbl_role.getSelectedRow();
         if(ExecuteData.DeleteRole(tbl_role.getValueAt(row, 0).toString()) && row >=0)
         {
             
            JOptionPane.showMessageDialog(null,"Xóa thành công !");
            showDataOnTable(tbl_role,x.getDataRole ());
        }
        else
            JOptionPane.showMessageDialog(null,"Bạn chưa chọn dòng");
    
    }//GEN-LAST:event_btn_XoaActionPerformed

    private void btn_VPDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_VPDActionPerformed
        // TODO add your handling code here:
        VPD t = new VPD();
        t.setVisible(true);
    }//GEN-LAST:event_btn_VPDActionPerformed

    private void btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refreshActionPerformed
        // TODO add your handling code here:
        showDataOnTable(tbl_role,x.getDataRole ());
    }//GEN-LAST:event_btn_refreshActionPerformed

    private void btn_QuyenleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_QuyenleActionPerformed
        // TODO add your handling code here:
        Privileges_ t = new Privileges_();
        t.setVisible(true);
    }//GEN-LAST:event_btn_QuyenleActionPerformed

    private void cbo_UsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_UsernameActionPerformed
        // TODO add your handling code here:
        showDataOnTable(tbl_userofrole,x.getRoleofUser(cbo_Username.getSelectedItem().toString()));

    }//GEN-LAST:event_cbo_UsernameActionPerformed

    private void Control_ROLEComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_Control_ROLEComponentShown
        // TODO add your handling code here:
        loadDataProfile();
        showDataOnTable(tbl_userofrole,x.getRoleofUser(cbo_Username.getSelectedItem().toString()));
    }//GEN-LAST:event_Control_ROLEComponentShown

    private void btn_defaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_defaultActionPerformed
        // TODO add your handling code here:
        int row = tbl_userofrole.getSelectedRow();
        if(row < 0 )
        {
            JOptionPane.showMessageDialog(null,"Bạn chưa chọn dòng !");
        }
        else
        {
            String user = cbo_Username.getSelectedItem().toString();
            String role = tbl_userofrole.getValueAt(row, 1).toString();
            if(rdo_default.isSelected())
            {
                if(up.defaultAllRole(user ))
                {
                    JOptionPane.showMessageDialog(null,"Đặt mặt định thành công");
                    showDataOnTable(tbl_userofrole,x.getRoleofUser(user));

                }
            }
            else
            {
                if(up.defaultRole(user,role ))
                {
                    JOptionPane.showMessageDialog(null,"Đặt mặt định thành công");
                    showDataOnTable(tbl_userofrole,x.getRoleofUser(user));

                }
            }
            
        }
        
    }//GEN-LAST:event_btn_defaultActionPerformed

    private void btn_TaoMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_TaoMoiActionPerformed
        if(fCrePro == null)
        fCrePro = new CreateProfile();
        fCrePro.setVisible(true);
        fCrePro.root = this;
    }//GEN-LAST:event_btn_TaoMoiActionPerformed

    private void btn_CapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CapNhatActionPerformed
        UpdateProfile f = new UpdateProfile();
        f.nameProfile = cb_ListProfile.getSelectedItem().toString();
        f.root = this;
        f.setVisible(true);
    }//GEN-LAST:event_btn_CapNhatActionPerformed

    private void btn_XoaProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XoaProfileActionPerformed
        String nameProfile = cb_ListProfile.getSelectedItem().toString();
        if(JOptionPane.showConfirmDialog(this, "Bạn chắc chắn xóa Profile: "+nameProfile) == JOptionPane.YES_OPTION)
        if(ExecuteData.DeleteProfile(nameProfile)){
            JOptionPane.showMessageDialog(this, "Xóa Profile '"+ nameProfile +"' thành công !");
            loadDataProfile();
        }
        else
        JOptionPane.showMessageDialog(this, "Xóa Profile '"+ nameProfile +"' thất bại !");
    }//GEN-LAST:event_btn_XoaProfileActionPerformed

    private void cb_ListProfileItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_ListProfileItemStateChanged
        ArrayList temp = x.getDetailsProfile(evt.getItem().toString());
        DefaultTableModel model = (DefaultTableModel)table_Profile.getModel();
        model.setDataVector((Object[][])temp.get(1), (Object[])temp.get(0));
    }//GEN-LAST:event_cb_ListProfileItemStateChanged

    private void cboObjectNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboObjectNameItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboObjectNameItemStateChanged

    private void btnCreateAuditPolicyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateAuditPolicyActionPerformed
        // TODO add your handling code here:
        try
        {
            String objectName=cboObjectName.getSelectedItem().toString();

            if(txtPolicyName.getText().isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Vui long nhap ten!","Canh bao",JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                for(Component com : AUDIT.getComponents())
                {
                    if(com instanceof JCheckBox)
                    {
                        JCheckBox box = (JCheckBox)com;
                        if(box.isSelected ())
                        {
                            statement += box.getText()+",";
                        }
                    }

                }
                String realStatement=statement.substring(0,statement.length()-1);
                String a=cboObjectName.getSelectedItem().toString();
                String b=txtPolicyName.getText();
                String c=realStatement;
                String d=cboObjectSchema.getSelectedItem().toString();
                y.CreateAuditPolicy(d,a,b,c);
                JOptionPane.showMessageDialog(this, "Thiết lập policy thành công!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, "Thiet lap audit policy thất bại !","Thông Báo",JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_btnCreateAuditPolicyActionPerformed

    private void btn_showauditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_showauditMouseClicked
        // TODO add your handling code here:
        ShowAuditPolicy saup = new ShowAuditPolicy ();
        saup.setVisible (true);
    }//GEN-LAST:event_btn_showauditMouseClicked

    private void btn_showauditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_showauditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_showauditActionPerformed

    private void btn_XemTTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_XemTTMouseClicked
        // TODO add your handling code here:
        ShowAudit sau = new ShowAudit ();
        sau.setVisible (true);
    }//GEN-LAST:event_btn_XemTTMouseClicked

    private void cboObjectSchemaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboObjectSchemaItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboObjectSchemaItemStateChanged
    private void LoadComboBoxObjectSchema()
    {
        ArrayList objectSchema=x.getObjectSchema();
        for(var e:objectSchema)
        {
            cboObjectSchema.addItem(e.toString());
        }
    }
    private void AUDITComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_AUDITComponentShown
        LoadComboBoxObjectSchema();
        LoadComboBoxObjectName();
    }//GEN-LAST:event_AUDITComponentShown

    private void btnCreateStandardAuditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateStandardAuditActionPerformed
        // TODO add your handling code here:
        try
        {
            String objectName=cboObjectName.getSelectedItem().toString();
            for(Component com : AUDIT.getComponents())
                {
                    if(com instanceof JCheckBox)
                    {
                        JCheckBox box = (JCheckBox)com;
                        if(box.isSelected ())
                        {
                            statement += box.getText()+",";
                        }
                    }

                }
                String realStatement=statement.substring(0,statement.length()-1);
                String a=cboObjectName.getSelectedItem().toString();
                String b=realStatement;
                String c=cboObjectSchema.getSelectedItem().toString();
                y.CreateStandardAudit(b,c,a);
                JOptionPane.showMessageDialog(this, "Thiết lập policy thành công!","Thông Báo",JOptionPane.INFORMATION_MESSAGE);
        }
        catch(Exception e)
                {
                    JOptionPane.showMessageDialog(this, "Thiet lap standard audit thất bại !","Thông Báo",JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
                }
                
    }//GEN-LAST:event_btnCreateStandardAuditActionPerformed
    EditAuditPolicy f=null;
    private void btnEditAuditPolicyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditAuditPolicyActionPerformed
        // TODO add your handling code here:
        if(f==null)
            f=new EditAuditPolicy();
        f.setVisible(true);
    }//GEN-LAST:event_btnEditAuditPolicyActionPerformed

    private void btn_InsertPrivActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_InsertPrivActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_InsertPrivActionPerformed

    private void cbo_UsernameComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_cbo_UsernameComponentShown
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cbo_UsernameComponentShown

    private void btn_RevokeRoleToUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_RevokeRoleToUserActionPerformed
        // TODO add your handling code here:
       int row = tbl_userofrole.getSelectedRow();
       if(row < 0)
       {
            JOptionPane.showMessageDialog(null,"Bạn chưa chọn dòng !");
       }
       else
       {
           String user = cbo_Username.getSelectedItem().toString();
           if(up.revokeRoleToUser(tbl_userofrole.getValueAt(row, 1).toString(), user))
           {
               JOptionPane.showMessageDialog(null,"Thu hồi role từ user thành công!");
               showDataOnTable(tbl_userofrole,x.getRoleofUser(user));
           }
           else
           {
                JOptionPane.showMessageDialog(null,"Thu hồi role từ user thành công!");
           }
       }
        
    }//GEN-LAST:event_btn_RevokeRoleToUserActionPerformed

    private void btn_grantSelectRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_grantSelectRoleActionPerformed
        // TODO add your handling code here:
        int row = table_User.getSelectedRow();
       if(row < 0)
       {
            JOptionPane.showMessageDialog(null,"Bạn chưa chọn dòng !");
       }
       else
       {
           String user = table_User.getValueAt(row, 1).toString();
           if(ExecuteData.GrantSelect_dba_Role_priv(user))
           {
               JOptionPane.showMessageDialog(null,"Cấp quyền thành công!");

           }
       }
        
    }//GEN-LAST:event_btn_grantSelectRoleActionPerformed

    private void LoadComboBoxUserName()
    {
        ArrayList tableName = x.getAllUserName();
        for(var e:tableName)
        {
            cbo_Username.addItem(e.toString());
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AUDIT;
    private javax.swing.JPanel AUDIT1;
    private javax.swing.JPanel Backup_Restore;
    private javax.swing.JPanel Control_ROLE;
    private javax.swing.JPanel PROFILE;
    private javax.swing.JPanel Panel2;
    private javax.swing.JPanel QL_ROLE;
    private javax.swing.JPanel SESSION;
    private javax.swing.JPanel TTHT;
    private javax.swing.JPanel USER;
    private Ultilities.swing.Controls.buttonCustom btnClearCMD;
    private javax.swing.JButton btnCreateAuditPolicy;
    private javax.swing.JButton btnCreateAuditPolicy1;
    private javax.swing.JButton btnCreateStandardAudit;
    private Ultilities.swing.Controls.ButtonGradient btnDemo;
    private javax.swing.JButton btnEditAuditPolicy;
    private Ultilities.swing.Controls.ButtonGradient btnRefresh;
    private Ultilities.swing.Controls.ButtonGradient btn_CapNhat;
    private javax.swing.JButton btn_Create;
    private Ultilities.swing.Controls.ButtonGradient btn_CreateUser;
    private Ultilities.swing.Controls.buttonCustom btn_Export;
    private javax.swing.JButton btn_InsertPriv;
    private Ultilities.swing.Controls.buttonCustom btn_LoadListBackup;
    private javax.swing.JButton btn_Quyenle;
    private Ultilities.swing.Controls.ButtonGradient btn_RemoveUser;
    private javax.swing.JButton btn_Revoke;
    private javax.swing.JButton btn_RevokeRoleToUser;
    private Ultilities.swing.Controls.ButtonGradient btn_TaoMoi;
    private javax.swing.JButton btn_VPD;
    private javax.swing.JButton btn_XemTT;
    private javax.swing.JButton btn_XemTT1;
    private javax.swing.JButton btn_Xoa;
    private Ultilities.swing.Controls.ButtonGradient btn_XoaProfile;
    private Ultilities.swing.Controls.buttonCustom btn_backup;
    private Ultilities.swing.Controls.buttonCustom btn_browse;
    private javax.swing.JButton btn_default;
    private javax.swing.JButton btn_grantSelectRole;
    private Ultilities.swing.Controls.buttonCustom btn_import;
    private Ultilities.swing.Controls.buttonCustom btn_recovery;
    private javax.swing.JButton btn_refresh;
    private Ultilities.swing.Controls.buttonCustom btn_showAllDataFileBKRS;
    private javax.swing.JButton btn_showaudit;
    private javax.swing.JButton btn_showaudit1;
    private javax.swing.JButton btn_updaterole;
    private Ultilities.swing.Controls.ButtonGradient buttonGradient1;
    private javax.swing.ButtonGroup buttonGroup1;
    private Ultilities.swing.Controls.ComboBoxSuggestion cb_Dir;
    private Ultilities.swing.Controls.ComboBoxSuggestion cb_ListProfile;
    private Ultilities.swing.Controls.ComboBoxSuggestion cb_TableName_IE;
    private javax.swing.JCheckBox cbk_update;
    private javax.swing.JComboBox<String> cboObjectName;
    private javax.swing.JComboBox<String> cboObjectName1;
    private javax.swing.JComboBox<String> cboObjectSchema;
    private javax.swing.JComboBox<String> cboObjectSchema1;
    private javax.swing.JComboBox<String> cbo_Username;
    private javax.swing.JComboBox<String> cbo_objectname;
    private javax.swing.JCheckBox chkBox_blockingUser;
    private javax.swing.JCheckBox chk_compressBK;
    private javax.swing.JCheckBox ckbDelete;
    private javax.swing.JCheckBox ckbDelete1;
    private javax.swing.JCheckBox ckbInsert;
    private javax.swing.JCheckBox ckbInsert1;
    private javax.swing.JCheckBox ckbSelect;
    private javax.swing.JCheckBox ckbUpdate;
    private javax.swing.JCheckBox ckbUpdate1;
    private javax.swing.JCheckBox ckb_all;
    private javax.swing.JCheckBox ckb_delete;
    private javax.swing.JCheckBox ckb_insert;
    private javax.swing.JCheckBox ckb_select;
    private javax.swing.JTextArea cmdScreen;
    private Ultilities.swing.Controls.ComboBoxSuggestion comboBoxSuggestion;
    private javax.swing.JLabel countRecord;
    private javax.swing.ButtonGroup grRadio_Backup;
    private javax.swing.ButtonGroup grRadio_Recover;
    private javax.swing.JPanel import_export;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lb_IE;
    private javax.swing.JLabel lb_slProfile;
    private javax.swing.JLabel lb_slProfile1;
    private javax.swing.JLabel lb_slProfile3;
    private javax.swing.JLabel lb_slProfile4;
    private javax.swing.JLabel lb_slProfile5;
    private javax.swing.JLabel lb_slUser;
    private javax.swing.JLabel lbl_Pass;
    private javax.swing.JLabel numRecord_tab2;
    private javax.swing.JRadioButton rad_bkFull;
    private javax.swing.JRadioButton rad_bkFullArchivelog;
    private javax.swing.JRadioButton rad_datapump;
    private javax.swing.JRadioButton rad_sqlcl;
    private javax.swing.JRadioButton rd_incre;
    private javax.swing.JRadioButton rd_lv0;
    private javax.swing.JRadioButton rd_lv1;
    private javax.swing.JRadioButton rdo_NotPass;
    private javax.swing.JRadioButton rdo_Pass;
    private javax.swing.JRadioButton rdo_default;
    private Ultilities.swing.Controls.Table table1;
    private Ultilities.swing.Controls.Table table_ImEx;
    private Ultilities.swing.Controls.Table table_Profile;
    private Ultilities.swing.Controls.Table table_User;
    private Ultilities.swing.Controls.Table table_dataFile;
    private Ultilities.swing.Controls.Table table_tab2;
    private Ultilities.swing.Controls.Table tbl_role;
    private Ultilities.swing.Controls.Table tbl_userofrole;
    private javax.swing.JTextField txtPolicyName;
    private javax.swing.JTextField txtPolicyName1;
    private javax.swing.JFormattedTextField txt_Date;
    private javax.swing.JTextField txt_Pass;
    private javax.swing.JTextField txt_pathFile;
    private javax.swing.JTextField txt_role_name;
    private javax.swing.JFormattedTextField txt_time;
    // End of variables declaration//GEN-END:variables
}
