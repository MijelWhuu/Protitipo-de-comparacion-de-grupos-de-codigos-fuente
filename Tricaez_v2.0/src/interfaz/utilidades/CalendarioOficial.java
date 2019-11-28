package interfaz.utilidades;

import baseGeneral.ErrorEntity;
import bl.utilidades.CalendarioOficialBl;
import entitys.DiasInhabiles;
import entitys.SesionEntity;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class CalendarioOficial extends javax.swing.JInternalFrame {
    
    CalendarioOficialBl bl = new CalendarioOficialBl();
    SesionEntity sesion;
    
    private void cargaMes(int anio,int mes, javax.swing.JPanel panelMes){
        int valorInicialTabla;
        String [] dayLabels = {"Dom", "Lun", "Mar", "Mie", "Jue", "Vie", "Sáb"};

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.MONTH,mes);
        calendar.set(Calendar.YEAR,anio);
        int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
        int diasEnMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int agrega = (49 - (diasEnMes + 7 + (diaSemana - 1)));

        for(valorInicialTabla=1; (diaSemana+7)%7 != Calendar.SUNDAY; diaSemana--)
            valorInicialTabla--;

        panelMes.setLayout(new java.awt.GridLayout(0,7));

        for(int i=0; i<7; i++)
            panelMes.add(new javax.swing.JLabel(dayLabels[i],javax.swing.JLabel.CENTER));

        for(int i=valorInicialTabla; i<=0; i++)
            panelMes.add(new javax.swing.JLabel(""));
        
        ArrayList array = bl.getDiasInhabiles(anio, mes+1);
        
        for(int i=1; i<=diasEnMes; i++){
            boolean inhabil = false;
            if(array.size()>0){
                for(int j=0; j<array.size(); j++){
                    DiasInhabiles inh = (DiasInhabiles) array.get(j);
                    if(i == inh.getDia())
                        inhabil = true;
                }
            }
            panelMes.add(new cargaDia(anio,mes,i,inhabil));
        }
            
        for(int i=0; i<agrega; i++)
            panelMes.add(new javax.swing.JLabel(""));
    }
    
    public class cargaDia extends javax.swing.JButton implements ActionListener{

        int year;
        int month;
        int day;
        
        public cargaDia(int year, int month, int day, boolean inhabil) {

            this.year = year;
            this.month = month;
            this.day = day;

            this.addActionListener(this);
            
            this.setLabel(""+day);
            if(obtenerDiaSemana(year, month, day)==Calendar.SUNDAY || obtenerDiaSemana(year, month, day)==Calendar.SATURDAY){
                this.setBackground(Color.RED);
                this.setToolTipText("Día no laborable");
            }else if(inhabil){
                this.setBackground(Color.BLACK);
                this.setToolTipText("Día inhábil");
            }
        }
        
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            if(obtenerDiaSemana(year, month, day)!=Calendar.SUNDAY && obtenerDiaSemana(year, month, day)!=Calendar.SATURDAY){
                
                if(this.getBackground()==Color.BLACK){
                    ErrorEntity error = bl.deleteFecha(year, month+1, day);
                    if(!error.getError()){
                        this.setBackground(null);
                        this.setToolTipText("");
                    }
                }else{
                    ErrorEntity error = bl.saveFecha(year, month+1, day, sesion.getId_usuario(), sesion.getFechaActual());
                    if(!error.getError()){
                        this.setBackground(Color.BLACK);
                        this.setToolTipText("Día inhábil");
                    }
                }
            }
        }
    }
    
    private int obtenerDiaSemana(int anio, int mes, int dia){
        int numeroDia=0;
        Calendar cale = Calendar.getInstance(TimeZone.getDefault());
        cale.set(Calendar.DAY_OF_MONTH,dia);
        cale.set(Calendar.MONTH,mes);
        cale.set(Calendar.YEAR,anio);
        numeroDia=cale.get(Calendar.DAY_OF_WEEK);
        return numeroDia;
    }
    
    private void removeComponentPanel(javax.swing.JPanel panel){
        panel.removeAll();
        panel.revalidate();
    }
    
    private void cargaAnio(){
        javax.swing.JPanel[] paneles = {pEnero, pFebrero, pMarzo, pAbril, pMayo, pJunio, pJulio, pAgosto, pSeptiembre, pOctubre, pNoviembre, pDiciembre};
        for(int i=0; i<paneles.length; i++)
            removeComponentPanel(paneles[i]);
        int anio = new Integer(txtAnio.getText());
        for(int i=0; i<12; i++)
            cargaMes(anio,i, paneles[i]);
    }
    
    private void anioActual(int op){
        int anioAct, anioAnt, anioSig, anioActNew;
        
        if(op == 1){
            anioAct = new Integer(txtAnio.getText());
            anioAnt = anioAct - 2;
            anioActNew = anioAct - 1;
            anioSig = anioAct;
        }else{
            anioAct = new Integer(txtAnio.getText());
            anioAnt = anioAct;
            anioActNew = anioAct + 1;
            anioSig = anioAct + 2;
        }
        btnAnioAnt.setText("" + anioAnt);
        txtAnio.setText("" + anioActNew);
        btnAnioSig.setText("" + anioSig);
        cargaAnio();
    }
    

    /**
     * Creates new form CalendarioOficial
     */
    
    
    public CalendarioOficial() {
    }

    public CalendarioOficial(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        txtAnio.setText("" + c.get(Calendar.YEAR));
        Integer anioA = c.get(Calendar.YEAR);
        btnAnioAnt.setText("" + (anioA-1));
        btnAnioSig.setText("" + (anioA+1));
        cargaAnio();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        pAbril = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        pMayo = new javax.swing.JPanel();
        Junio = new javax.swing.JLabel();
        pJunio = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        pEnero = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        pFebrero = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        pMarzo = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        pJulio = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        pAgosto = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        pSeptiembre = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        pOctubre = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        pNoviembre = new javax.swing.JPanel();
        Junio1 = new javax.swing.JLabel();
        pDiciembre = new javax.swing.JPanel();
        btnAnioAnt = new javax.swing.JButton();
        btnAnioSig = new javax.swing.JButton();
        txtAnio = new javax.swing.JTextField();

        setClosable(true);
        setTitle("Calendario Oficial");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/CalendarioOficial.jpg"))); // NOI18N

        pAbril.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pAbrilLayout = new javax.swing.GroupLayout(pAbril);
        pAbril.setLayout(pAbrilLayout);
        pAbrilLayout.setHorizontalGroup(
            pAbrilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 296, Short.MAX_VALUE)
        );
        pAbrilLayout.setVerticalGroup(
            pAbrilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel5.setBackground(new java.awt.Color(255, 0, 102));
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Abril");

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Mayo");

        pMayo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pMayoLayout = new javax.swing.GroupLayout(pMayo);
        pMayo.setLayout(pMayoLayout);
        pMayoLayout.setHorizontalGroup(
            pMayoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 296, Short.MAX_VALUE)
        );
        pMayoLayout.setVerticalGroup(
            pMayoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Junio.setBackground(new java.awt.Color(255, 255, 255));
        Junio.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Junio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Junio.setText("Junio");

        pJunio.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pJunioLayout = new javax.swing.GroupLayout(pJunio);
        pJunio.setLayout(pJunioLayout);
        pJunioLayout.setHorizontalGroup(
            pJunioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 297, Short.MAX_VALUE)
        );
        pJunioLayout.setVerticalGroup(
            pJunioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 296, Short.MAX_VALUE)
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Enero");

        pEnero.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pEneroLayout = new javax.swing.GroupLayout(pEnero);
        pEnero.setLayout(pEneroLayout);
        pEneroLayout.setHorizontalGroup(
            pEneroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pEneroLayout.setVerticalGroup(
            pEneroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 296, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Febrero");

        pFebrero.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pFebreroLayout = new javax.swing.GroupLayout(pFebrero);
        pFebrero.setLayout(pFebreroLayout);
        pFebreroLayout.setHorizontalGroup(
            pFebreroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pFebreroLayout.setVerticalGroup(
            pFebreroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Marzo");

        pMarzo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pMarzoLayout = new javax.swing.GroupLayout(pMarzo);
        pMarzo.setLayout(pMarzoLayout);
        pMarzoLayout.setHorizontalGroup(
            pMarzoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pMarzoLayout.setVerticalGroup(
            pMarzoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel7.setBackground(new java.awt.Color(255, 0, 102));
        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Julio");

        pJulio.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pJulioLayout = new javax.swing.GroupLayout(pJulio);
        pJulio.setLayout(pJulioLayout);
        pJulioLayout.setHorizontalGroup(
            pJulioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 296, Short.MAX_VALUE)
        );
        pJulioLayout.setVerticalGroup(
            pJulioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Agosto");

        pAgosto.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pAgostoLayout = new javax.swing.GroupLayout(pAgosto);
        pAgosto.setLayout(pAgostoLayout);
        pAgostoLayout.setHorizontalGroup(
            pAgostoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pAgostoLayout.setVerticalGroup(
            pAgostoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Septiembre");

        pSeptiembre.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pSeptiembreLayout = new javax.swing.GroupLayout(pSeptiembre);
        pSeptiembre.setLayout(pSeptiembreLayout);
        pSeptiembreLayout.setHorizontalGroup(
            pSeptiembreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pSeptiembreLayout.setVerticalGroup(
            pSeptiembreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 296, Short.MAX_VALUE)
        );

        jLabel10.setBackground(new java.awt.Color(255, 0, 102));
        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Octubre");

        pOctubre.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pOctubreLayout = new javax.swing.GroupLayout(pOctubre);
        pOctubre.setLayout(pOctubreLayout);
        pOctubreLayout.setHorizontalGroup(
            pOctubreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pOctubreLayout.setVerticalGroup(
            pOctubreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Noviembre");

        pNoviembre.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pNoviembreLayout = new javax.swing.GroupLayout(pNoviembre);
        pNoviembre.setLayout(pNoviembreLayout);
        pNoviembreLayout.setHorizontalGroup(
            pNoviembreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 296, Short.MAX_VALUE)
        );
        pNoviembreLayout.setVerticalGroup(
            pNoviembreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Junio1.setBackground(new java.awt.Color(255, 255, 255));
        Junio1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Junio1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Junio1.setText("Diciembre");

        pDiciembre.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pDiciembreLayout = new javax.swing.GroupLayout(pDiciembre);
        pDiciembre.setLayout(pDiciembreLayout);
        pDiciembreLayout.setHorizontalGroup(
            pDiciembreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pDiciembreLayout.setVerticalGroup(
            pDiciembreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 296, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(pAbril, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pMayo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pJunio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(pEnero, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                                    .addComponent(pFebrero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(pMarzo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                    .addComponent(Junio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(pJulio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                                    .addComponent(pAgosto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                    .addComponent(pSeptiembre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addContainerGap(54, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                            .addComponent(pOctubre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pNoviembre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Junio1, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                            .addComponent(pDiciembre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pEnero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pFebrero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pMarzo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Junio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pAbril, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pMayo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pJunio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pJulio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pAgosto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pSeptiembre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Junio1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pOctubre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pNoviembre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pDiciembre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        btnAnioAnt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAnioAnt.setText("2014");
        btnAnioAnt.setToolTipText("Año anterior");
        btnAnioAnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnioAntActionPerformed(evt);
            }
        });

        btnAnioSig.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAnioSig.setText("2016");
        btnAnioSig.setToolTipText("Año siguiente");
        btnAnioSig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnioSigActionPerformed(evt);
            }
        });

        txtAnio.setFont(new java.awt.Font("Tahoma", 1, 26)); // NOI18N
        txtAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAnio.setText("2015");
        txtAnio.setToolTipText("Año actual");
        txtAnio.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtAnio.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtAnio.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(327, 327, 327)
                .addComponent(btnAnioAnt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAnioSig, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAnioAnt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAnioSig, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAnioAntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnioAntActionPerformed
        anioActual(1);
    }//GEN-LAST:event_btnAnioAntActionPerformed

    private void btnAnioSigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnioSigActionPerformed
        anioActual(2);
    }//GEN-LAST:event_btnAnioSigActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Junio;
    private javax.swing.JLabel Junio1;
    private javax.swing.JButton btnAnioAnt;
    private javax.swing.JButton btnAnioSig;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pAbril;
    private javax.swing.JPanel pAgosto;
    private javax.swing.JPanel pDiciembre;
    private javax.swing.JPanel pEnero;
    private javax.swing.JPanel pFebrero;
    private javax.swing.JPanel pJulio;
    private javax.swing.JPanel pJunio;
    private javax.swing.JPanel pMarzo;
    private javax.swing.JPanel pMayo;
    private javax.swing.JPanel pNoviembre;
    private javax.swing.JPanel pOctubre;
    private javax.swing.JPanel pSeptiembre;
    private javax.swing.JTextField txtAnio;
    // End of variables declaration//GEN-END:variables
}
