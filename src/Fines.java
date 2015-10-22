
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Komani
 */
public class Fines extends javax.swing.JFrame {

    /**
     * Creates new form Fines
     */
     DefaultTableModel model;
      String selecteditem = "All Fines";
    public Fines() {
        initComponents();
        
        model = (DefaultTableModel) jTable1.getModel();
         TableColumn includeColumn = jTable1.getColumnModel().getColumn(8);

        includeColumn.setCellEditor(jTable1.getDefaultEditor(Boolean.class));
         includeColumn.setCellRenderer(jTable1.getDefaultRenderer(Boolean.class)); 
                       
        jScrollPane1.setVisible(false);
        jButton4.setVisible(false);
        jButton3.setVisible(false);
        jTable1.getTableHeader().setFont( new java.awt.Font( "Tahoma" , java.awt.Font.PLAIN, 18 ));
        jTable1.setBackground(Color.WHITE);
        jTable1.setShowHorizontalLines(true);
        jTable1.setShowVerticalLines(true);
           setLocationRelativeTo(null);
        
        jButton3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                      jTextField1.setText("");
                      jTextField2.setText("");
                      jTextField3.setText("");
                      jScrollPane1.setVisible(false);
                      jButton4.setVisible(false);
                      jButton3.setVisible(false);
                      jLabel6.setText("");
                      
                     jLabel7.setText("");
                }
            });
        
        jButton4.addActionListener(new ActionListener() {
            
			public void actionPerformed(ActionEvent e) {
                            jLabel7.setText("");
                            try {
			 
                            Class.forName("com.mysql.jdbc.Driver");
 
                        } catch (ClassNotFoundException ed) {
 
                            System.out.println("Where is your MySQL JDBC Driver? "
					+ "Include in your library path!");
                            ed.printStackTrace();
                            return;
                        }
                    jLabel7.setText("");
		PreparedStatement stmt = null;
                    String sql;int flag = 1;
                   
                            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root","")) {

                               
                                   int checked = 0;
                                   int rows=jTable1.getRowCount();
                                    int i=0;
                                        while(i<rows){
                                            Boolean b = ((Boolean) jTable1.getValueAt(i, 8));
                                            if(b)
                                            {
                                                checked++;
                                              sql = "select loan_id from book_loans natural join fines where loan_id = "+jTable1.getValueAt(i, 3) +" and paid = 0 and date_in is null;" ;
                                                PreparedStatement stm = conn.prepareStatement(sql);
                                                ResultSet rt = stm.executeQuery();
                                                 if(rt.next()){
                                                     jLabel7.setText("You cant pay the fine unless you return the book.");
                                                     flag = 0;
                                                     break;
                                                 } else {
                                                
                                                sql = "update fines set paid = 1 where loan_id = "+ jTable1.getValueAt(i, 3)+" ;";
                                                Statement st = conn.createStatement();
                                                st.executeUpdate(sql);
                                                 }
                                            }
  
                                             i++;
                                            }
                                            if(flag == 1){
                                                   
                                                if(checked==0){
                                                     jLabel7.setText("*Atleast one fine must be selected.");
                                                } else {
                                                    jLabel7.setText("");
                                                      jLabel6.setText("Fines Paid Successfully!!");
                                                }
                                            }
                                        
    
                                System.out.println("Connection established");
                                   conn.close();
                            }catch(SQLException es){
			
			es.printStackTrace();
                    } 
                
                }
            });
        
               jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                       setVisible(false);
                       new LibraryGUI().setVisible(true);
                }
            });
           
        
        jButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                            jLabel7.setText("");
                            jLabel6.setText("");
                                
                          String cardno = jTextField1.getText();
                    String fname= jTextField2.getText();
                    String lname = jTextField3.getText();
                    String sql_paid = "", sql_npaid = "", sql = "";
                    jLabel5.setText(" ");
                    int rows = jTable1.getRowCount();
                     for(int i =rows-1; i>=0;i--)model.removeRow(i);
                  
                        try {
			 
                            Class.forName("com.mysql.jdbc.Driver");
 
                        } catch (ClassNotFoundException ed) {
 
                            System.out.println("Where is your MySQL JDBC Driver? "
					+ "Include in your library path!");
                            ed.printStackTrace();
                            return;
                        }
                        
                        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root","")) {
                        if(cardno.isEmpty() && fname.isEmpty() && lname.isEmpty()){
                                  jScrollPane1.setVisible(true);
                      jButton4.setVisible(true);
                      jButton3.setVisible(true);
                      
                    
                            if(selecteditem.equals("All Fines")){
                                 sql_paid = "select card_no,fname,lname,loan_id, book_id, title, author_name, fine_amt from book NATURAL JOIN book_authors NATURAL JOIN book_loans Natural join fines Natural join borrower where paid = 1;";
                                 PreparedStatement stmt = conn.prepareStatement(sql_paid);
                                 ResultSet r = stmt.executeQuery();
                                 
                              
                                     
                                 while(r.next())
                                 model.addRow(new Object[]{r.getString(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5), r.getString(6),r.getString(7), r.getString(8),Boolean.FALSE});
                            }
                                 sql_npaid = "select card_no,fname,lname,loan_id, book_id, title, author_name, fine_amt from book NATURAL JOIN book_authors NATURAL JOIN book_loans Natural join fines Natural join borrower where paid = 0;";
                                 
                                 PreparedStatement stmtn = conn.prepareStatement(sql_npaid);
                                 ResultSet rn = stmtn.executeQuery();
                                 while(rn.next())
                                 model.addRow(new Object[]{rn.getString(1), rn.getString(2), rn.getString(3), rn.getString(4),rn.getString(5),rn.getString(6), rn.getString(7),rn.getString(8),Boolean.FALSE});
                                } else if(!cardno.isEmpty()) {
                                    sql = "select fname from borrower where Card_no = "+Integer.parseInt(cardno)+";";
                                   
                                    PreparedStatement stmt = conn.prepareStatement(sql);
                                    ResultSet rs = stmt.executeQuery();
                                    if(!rs.next()){
                                        jLabel5.setText("* Card no. does not exist.");
                                        
                                    } else {
                                          jScrollPane1.setVisible(true);
                      jButton4.setVisible(true);
                      jButton3.setVisible(true);
                                        if(selecteditem.equals("All Fines")){
                                        sql_paid = "select card_no,fname,lname,loan_id, book_id, title, author_name, fine_amt from book NATURAL JOIN book_authors NATURAL JOIN book_loans Natural join fines Natural join borrower where card_no = "+cardno+" and paid = 1;";
                                       
                                       PreparedStatement stmtp = conn.prepareStatement(sql_paid);
                                        ResultSet r = stmtp.executeQuery();
                                        while(r.next())
                                            model.addRow(new Object[]{r.getString(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5),r.getString(6),r.getString(7),r.getString(8),Boolean.FALSE});
                                        }
                                        
                                         sql_npaid = "select card_no,fname,lname,loan_id, book_id, title, author_name, fine_amt from book NATURAL JOIN book_authors NATURAL JOIN book_loans Natural join fines Natural join borrower where card_no = "+cardno+" and paid = 0;";
                                        PreparedStatement stmtn = conn.prepareStatement(sql_npaid);
                                            ResultSet rn = stmtn.executeQuery();
                                            while(rn.next())
                                            model.addRow(new Object[]{rn.getString(1), rn.getString(2), rn.getString(3), rn.getString(4),rn.getString(5),rn.getString(6),rn.getString(7),rn.getString(8),Boolean.FALSE});
                                            
                                    } 
                                }else if(!fname.isEmpty() && !lname.isEmpty()){
                                             sql = "select fname,lname from borrower where fname = '"+fname+"' and lname = '"+lname+"';";
                                   
                                    PreparedStatement stmt = conn.prepareStatement(sql);
                                    ResultSet rs = stmt.executeQuery();
                                    if(!rs.next()){
                                        jLabel5.setText("* Name does not exist.");
                                        
                                    } else {
                                          jScrollPane1.setVisible(true);
                      jButton4.setVisible(true);
                      jButton3.setVisible(true);
                                        if(selecteditem.equals("All Fines")){
                                        sql_paid = "select card_no,fname, lname, loan_id,book_id, title, author_name, fine_amt from book NATURAL JOIN book_authors NATURAL JOIN book_loans Natural join fines Natural join borrower where fname = '"+fname+"' and lname = '"+lname+"' and paid = 1;";
                                  
                                        PreparedStatement stmtp = conn.prepareStatement(sql_paid);
                                        ResultSet r = stmtp.executeQuery();
                                        while(r.next())
                                        model.addRow(new Object[]{r.getString(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5), r.getString(6),r.getString(7),r.getString(8),Boolean.FALSE});
                                        }
                                         sql_npaid = "select card_no,fname, lname,loan_id, book_id, title, author_name, fine_amt from book NATURAL JOIN book_authors NATURAL JOIN book_loans Natural join fines Natural join borrower where fname = '"+fname+"' and lname = '"+lname+"' and paid = 0;";
                                 
                                        PreparedStatement stmtn = conn.prepareStatement(sql_npaid);
                                            ResultSet rn = stmtn.executeQuery();
                                            while(rn.next())
                                            model.addRow(new Object[]{rn.getString(1), rn.getString(2), rn.getString(3), rn.getString(4),rn.getString(5),rn.getString(6),rn.getString(7),rn.getString(8),Boolean.FALSE});
                                            
                                            }
                
                                }

                                System.out.println("Connection established");

                          conn.close();
		
                    }catch(SQLException es){
			
			es.printStackTrace();
                    } 
                    }
            });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Check Fines");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Card No");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("First Name");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton1.setText("Home");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Card No", "First Name", "Last Name", "Loan ID", "Book ID", "Book Title", "Book Author", "Fine Amount", ""
            }
        ));
        jTable1.setRowHeight(45);
        jTable1.setRowMargin(3);
        jScrollPane1.setViewportView(jTable1);

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton2.setText("Check Fine");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All Fines", "Pending Fines" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Last Name");

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton3.setText("Clear");

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton4.setText("Pay");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 0, 0));
        jLabel5.setText("                                          ");

        jLabel6.setText("                          ");

        jLabel7.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 0, 0));
        jLabel7.setText("                                                                        ");

        jLabel8.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel8.setText("**You have to enter both first and last name");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 681, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(334, 334, 334)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(485, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel7))))
                        .addGap(20, 20, 20))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        selecteditem = (String) jComboBox1.getSelectedItem();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Fines.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Fines.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Fines.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Fines.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Fines().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
