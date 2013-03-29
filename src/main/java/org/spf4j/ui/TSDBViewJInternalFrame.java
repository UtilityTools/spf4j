/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.spf4j.ui;

import java.io.IOException;
import java.util.Collection;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.spf4j.perf.tsdb.ColumnInfo;
import org.spf4j.perf.tsdb.TimeSeriesDatabase;

/**
 *
 * @author zoly
 */
public class TSDBViewJInternalFrame extends javax.swing.JInternalFrame {

    private final TimeSeriesDatabase tsDb;
    
    /**
     * Creates new form TSDBViewJInternalFrame
     */
    public TSDBViewJInternalFrame(String databaseFile) throws IOException {
        super(databaseFile);
        initComponents();
        tsDb = new TimeSeriesDatabase(databaseFile, null);
        Collection<ColumnInfo> columnsInfo = tsDb.getColumnsInfo();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(databaseFile);
        for (ColumnInfo info : columnsInfo) {
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(info.getGroupName());
            for (String colName :info.getColumnNames()) {
                child.add(new DefaultMutableTreeNode(colName));
            }
            root.add(child);
        }
        measurementTree.setModel(new DefaultTreeModel(root));
        measurementTree.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rightPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        measurementTree = new javax.swing.JTree();

        org.jdesktop.layout.GroupLayout rightPanelLayout = new org.jdesktop.layout.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 448, Short.MAX_VALUE)
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 306, Short.MAX_VALUE)
        );

        setClosable(true);
        setMaximizable(true);
        setResizable(true);

        jScrollPane1.setViewportView(measurementTree);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree measurementTree;
    private javax.swing.JPanel rightPanel;
    // End of variables declaration//GEN-END:variables
}
