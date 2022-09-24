
class Polynomial{
    double [] coefficients;

    public Polynomial(){
        coefficients = new double[0];

    }
    public Polynomial(double [] arrayOfCoefficents){
        coefficients = new double[arrayOfCoefficents.length];
        for (int i = 0; i < arrayOfCoefficents.length; i++){
            coefficients[i] = arrayOfCoefficents[i];
        }
    }
    public Polynomial add(Polynomial polynomial2){
        Polynomial newPolynomial = new Polynomial();
        if(coefficients.length < polynomial2.coefficients.length){
            newPolynomial.coefficients = polynomial2.coefficients.clone();
            for (int i = 0; i < coefficients.length; i++){
                newPolynomial.coefficients[i] += coefficients[i];
            }
        }
        else{
            newPolynomial.coefficients = coefficients.clone();
            for (int i = 0; i < polynomial2.coefficients.length; i++){
                newPolynomial.coefficients[i] += polynomial2.coefficients[i];
            }
        }
        return newPolynomial;
    }
    public double evaluate(double x){
        double total = 0;
        for (int i = 0; i < coefficients.length; i++){
            total += coefficients[i]*Math.pow(x,i);
        }
        return total;
    }

    public boolean hasRoot(double x){
        return this.evaluate(x) == 0;
    }
}
