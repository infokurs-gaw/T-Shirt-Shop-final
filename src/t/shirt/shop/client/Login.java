/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t.shirt.shop.client;


/**
 *
 * @author marlon.rosendahl
 */
public class Login extends javax.swing.JFrame {

    /**
     * Creates new form Login
     */ClientController ctrl;
    public Login(ClientController ctrl) {
        initComponents();
        this.ctrl = ctrl;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tEmail = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        b_Login_Bestätigen = new javax.swing.JButton();
        tPasswort = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tEmail.setText("Benutzername");

        jLabel1.setText("LOGIN");

        b_Login_Bestätigen.setText("OK");
        b_Login_Bestätigen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                b_Login_BestätigenMouseClicked(evt);
            }
        });
        b_Login_Bestätigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_Login_BestätigenActionPerformed(evt);
            }
        });

        tPasswort.setText("jPasswordField1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(179, 179, 179)
                .addComponent(jLabel1)
                .addContainerGap(190, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tPasswort, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_Login_Bestätigen)
                    .addComponent(tEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tPasswort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(b_Login_Bestätigen)
                .addContainerGap(109, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_Login_BestätigenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_b_Login_BestätigenMouseClicked
        this.ctrl.logIn(tEmail.getText(),String.valueOf(tPasswort.getPassword()));
    }//GEN-LAST:event_b_Login_BestätigenMouseClicked

    private void b_Login_BestätigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_Login_BestätigenActionPerformed

    }//GEN-LAST:event_b_Login_BestätigenActionPerformed

    
    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_Login_Bestätigen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField tEmail;
    private javax.swing.JPasswordField tPasswort;
    // End of variables declaration//GEN-END:variables
}