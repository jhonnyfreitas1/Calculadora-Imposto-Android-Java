package com.example.calcularimpostoanual;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private double aliquota;
    private double rendaanual;
    private double despesas;
    private double parcelaDeduzir;
    private double IRRF;
    private double deducao;
    private float inss;
    private double despEnsino;
    private double pensao;
    private double impostopagar;
    private double impostorestituir;
    private double despMedic;
    private double valordepentes;
    private double rendabase;
    private int numDependentes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView resultado = findViewById(R.id.resultado);
        resultado.setVisibility(View.INVISIBLE);

        Button  calcular = (Button) findViewById(R.id.calcular);
        calcular.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText EDrendaanual = findViewById(R.id.rendaanual);
                EditText EDirrf = findViewById(R.id.irrf);
                EditText EDdependentes = findViewById(R.id.dependentes);
                EditText EDpensao = findViewById(R.id.pensao);
                EditText EDmedica = findViewById(R.id.medico);
                EditText EDensino = findViewById(R.id.ensino);
                EditText EDinss = findViewById(R.id.inss);


                setRendaAnual(String.valueOf(EDrendaanual.getText()));
                setIrrf(String.valueOf(EDirrf.getText()));
                setDependentes(String.valueOf(EDdependentes.getText()));
                setMedica(String.valueOf(EDmedica.getText()));
                setPensao(String.valueOf(EDpensao.getText()));
                setEnsino(String.valueOf(EDensino.getText()));

                CalculadoraImpostodeRenda(getRendaAnual(),getIRRF(),getDependentes(),getMedic(),getPensao(),getEnsino());
            }
        });
    }

        public void CalculadoraImpostodeRenda ( double rendaanual, double IRRF, int dependentes,
        double despmedic, double despPensao, double despEnsino){
            this.rendaanual = rendaanual;
            this.despMedic = despmedic;
            Dependentes(dependentes);
            Inss(rendaanual);
            despPensao(despPensao, rendaanual);
            despEnsino(despEnsino, dependentes);

            this.despesas = this.despMedic + this.pensao + this.despEnsino + this.inss + this.valordepentes;
            this.aliquota = Aliquota(rendaanual - this.despesas);
            this.rendabase = rendaanual - this.despesas;
            double imposto;
            imposto = (this.rendabase * (this.aliquota / 100)) - parcelaDeduzir(this.aliquota);

            double pagarxrestituir = imposto - IRRF;
            this.impostopagar = pagarxrestituir;
            this.impostorestituir = pagarxrestituir;

            TextView resultado = findViewById(R.id.resultado);
            resultado.setText("Imposto a pagar ou a reter: "+Double.toString(impostopagar)+"\n"+"Aliquota aplicada: "+this.aliquota+"%"+"\n"+"Despesas total: "+this.despesas
            +"\n Seu inss eh: "+ this.inss);
            resultado.setVisibility(View.VISIBLE);
        }

        public double parcelaDeduzir ( double aliquota){
            if (this.aliquota == 7.5) {
                this.deducao = 142.80 * 12;
                return (int) this.deducao;
            } else if (this.aliquota == 15) {
                this.deducao = 354.80 * 12;
                return (int) this.deducao;
            } else if (this.aliquota == 22.50) {
                this.deducao = 636.13 * 12;
                return (int) this.deducao;
            } else if (aliquota == 27.50) {
                this.deducao = 869.36 * 12;
                return (int) this.deducao;
            } else {
                TextView resultado = findViewById(R.id.resultado);
                resultado.setText("Voce esta isento sua renda eh abaixo da media para o calculo");
                resultado.setVisibility(View.VISIBLE);
            }
            return 0;
        }

        public double Aliquota ( double rendaanual){
            if (rendaanual < 22847.76) {
                System.out.println("Sua renda é abaixo do valor minimo para o calculo você está isento");
                return 0;
            } else if (rendaanual >= 22847.77 && rendaanual <= 33919.80) {
                this.aliquota = 7.5;
                return this.aliquota;
            } else if (rendaanual >= 33919.81 && rendaanual <= 45012.60) {
                this.aliquota = 15;
                return this.aliquota;
            } else if (rendaanual >= 45012.61 && rendaanual <= 55976.16) {
                this.aliquota = 22.50;
                return this.aliquota;
            } else if (rendaanual > 55976.16) {
                this.aliquota = 27.50;
                return this.aliquota;
            }
            return 0;
        }

        public int Dependentes ( int num){
            int valordepentes = (int) (num * 2275.08);
            this.valordepentes = valordepentes;
            return (int) this.valordepentes;
        }

        public float Inss ( double rendaanual){ //verifica o inss referente ao salario do usuario.
            double num = rendaanual;
            if (num < 100.0) {
                this.inss = 0;
            } else if (num < 21021) {
                double inss2 = (float) 8 / 100 * num;
                this.inss = (float) Math.round(inss2);
                return this.inss;
            } else if (num >= 21021 && num <= 35036) {
                double inss2 = (float) 9 / 100 * num;
                this.inss = (float) Math.round(inss2);
                return this.inss;

            } else if (num >= 35036 && num <= 70073) {
                double inss2 = (float) 11 / 100 * num;
                this.inss = (float) Math.round(inss2);
                return this.inss;
            } else if (num > 70073) {
                this.inss = (float) 73012;
                return this.inss;
            }
            return 0;
        }

        public double despEnsino ( double ensino, double dependentes){
            double limite = 3561.50;
            if (dependentes >= 1) {
                if (ensino > 1 + dependentes * limite) {
                    System.out.println("limite de 35% de despesas com ensino atingido");
                    return 0;
                } else {
                    this.despEnsino = ensino;
                    return this.despEnsino;
                }
            } else {
                if (ensino > limite) {
                    System.out.println("limite atingido");
                } else {
                    this.despEnsino = ensino;
                    return this.despEnsino;
                }
            }
            return 0;
        }

        public double despPensao ( double pensao, double rendaanual)
        {    //vê se a pensão é maior que 35% do salario comforme a lei. se nao deixa passar
            if (pensao > 1) {
                this.pensao = pensao;
                double renda = rendaanual;
                double limite = (float) 35 / 100 * this.rendaanual;
                if (pensao > limite) {
                    System.out.println("pensao maior que o limite de 35% que eh" + pensao + "limite eh" + limite);
                } else {
                    return pensao;
                }
            } else {
                return 0;
            }
            return 0;
        }

        public double getRendaAnual () {
            return this.rendaanual;
        }

        public double getInss () {
            return this.inss;
        }

        public double getDespesas () {
            return this.despesas;
        }

        public double getparcelaDeduzir () {
            return this.parcelaDeduzir;
        }
        public double getINSS () {
            return this.inss;
         }

        public double getImpostoPagar () {
            return this.impostopagar;
        }

        public double getImpostorestituir () {
            return this.impostorestituir;
        }

        public double getAliquota () {
            return this.aliquota;
        }

        public double getPensao () {
            return this.pensao;
        }
        public int getDependentes () {
            return this.numDependentes;
        }

        public double getMedic () {
            return this.despMedic;
        }

        public double getEnsino () {
            return this.despEnsino;
        }
        public double getIRRF () {
            return this.IRRF;
        }

        public double getRendaBase () {
            return this.rendabase;
        }
        public void setRendaAnual(String renda){

            if (renda.matches("")){
                this.rendaanual = 0;
            }else{
                this.rendaanual = Integer.parseInt(String.valueOf(Integer.parseInt(renda)));
            }

        }
        public void setIrrf(String irrf){

             if (irrf.matches("")) {
                this.IRRF = 0;
             }else{
                 this.IRRF = Integer.parseInt(String.valueOf(irrf));
             }
        }
        public void setDependentes(String dep){

            if (dep.matches("")){
                this.numDependentes = 0;
            }else{
                this.numDependentes = Integer.parseInt(String.valueOf(dep));
            }
        }
        public void setMedica(String med){

        if (med.matches("")){
                this.despMedic = 0;
            }else{
                this.despMedic = Integer.parseInt(String.valueOf(med));
            }
        }
        public void  setPensao(String pensao){

            if (pensao.matches("")){
                this.pensao = 0;
            }else{
                this.pensao = Integer.parseInt(String.valueOf(pensao));
            }

        }
        public void setEnsino(String ensino) {

            if(ensino.matches("")){
                this.despEnsino = 0;
            }else{
                this.despEnsino = Integer.parseInt(String.valueOf(ensino));
            }

        }
        public void setInss(String inss){
            if (inss.matches("")){
                this.inss = 0;
            }
        }
}
