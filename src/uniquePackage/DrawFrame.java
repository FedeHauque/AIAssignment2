
package uniquePackage;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class DrawFrame extends javax.swing.JFrame {
    private int[][] city_positions;
    private ArrayList<Integer> pathToDraw; 

    public DrawFrame(int[][] c, ArrayList<Integer> path) {
        initComponents();
        city_positions = c;
        pathToDraw = path;
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 462, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 349, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void paint(Graphics g){
        g.setColor(Color.BLACK);
        for(int i=0; i<city_positions.length; i++){
            g.fillOval(10+city_positions[i][0],250-city_positions[i][1],8,8);
        }
        g.setColor(Color.BLUE);
        for(int i=1; i<pathToDraw.size(); i++){
            g.drawLine(14+city_positions[pathToDraw.get(i)][0], 254-city_positions[pathToDraw.get(i)][1], 14+city_positions[pathToDraw.get(i-1)][0], 254-city_positions[pathToDraw.get(i-1)][1]);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
