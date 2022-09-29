import java.io.*;

class Polynomial{
    double [] coefficients;
    int [] powers;
    public Polynomial(){
        coefficients = new double[0];
        powers = new int[0];

    }
    public Polynomial(double [] arrayOfCoefficents){
            int nonzeroterms = 0;
            for (int i = 0; i < arrayOfCoefficents.length; i++) {
                if (arrayOfCoefficents[i] != 0) {
                    nonzeroterms++;
                }
            }
            coefficients = new double[nonzeroterms];
            powers = new int[nonzeroterms];
            for (int i = 0, j = 0; i < arrayOfCoefficents.length; i++) {
                if (arrayOfCoefficents[i] != 0) {
                    coefficients[j] = arrayOfCoefficents[i];
                    powers[j] = i;
                    j++;
                }
            }

    }
    public Polynomial(File file) throws IOException {
        BufferedReader input = new BufferedReader(new FileReader(file));
        String poly = input.readLine();
        input.close();
        int count = 1;
        if(poly.charAt(0) == '-')
            count = 0;
        for(int i = 0; i <poly.length();i++){
            if('+' == poly.charAt(i) || '-' == poly.charAt(i)){
                count++;
            }
        }
        poly = poly + "+";

        powers = new int[count];
        coefficients = new double [count];

        String currentDigit = "";
        int val = 1;
        int traversecoefficent = 0, traversepower = 0, firstoperator = 0, firstx = 0, start = 0;

        if(poly.charAt(0) == '-'){
            start = 1;
        }
        firstoperator = Math.min(poly.indexOf("+", start), poly.indexOf("-", start));
        firstx = poly.indexOf("x", start);

        if(firstoperator < firstx && firstoperator != -1 && firstx != -1){
            poly = poly.substring(0,firstoperator) + "x0" + poly.substring(firstoperator, poly.length());
        }

        firstoperator = Math.max(poly.indexOf("+x"), poly.indexOf("-x"));
        if(firstoperator != -1){
            poly = poly.substring(0,firstoperator+1) + "1" + poly.substring(firstoperator+1, poly.length());
        }
        firstx = Math.max(poly.indexOf("x+"), poly.indexOf("x-"));
        if(firstx != -1){
            poly = poly.substring(0,firstx+1) + "1" + poly.substring(firstx+1, poly.length());
        }
        for(int i = 0; i <poly.length();i++){
            char curr = poly.charAt(i);
            if(curr == '-') {
                val = -1;
                if(currentDigit.length() != 0){
                    powers[traversepower] = val * Integer.parseInt(currentDigit);
                    traversepower++;
                }
            }
            else if (curr == '+'){
                val = 1;
                if(currentDigit.length() != 0){
                    powers[traversepower] = val * Integer.parseInt(currentDigit);
                    traversepower++;
                }
                currentDigit = "";
            }
            else if (curr == 'x'){
                if(currentDigit.length() != 0){
                    coefficients[traversecoefficent] = val * Double.parseDouble(currentDigit);
                    traversecoefficent++;
                }
                currentDigit = "";
            }
            else{
                currentDigit = currentDigit + curr;
            }
        }
    }
    public void saveToFile(String fileName) throws IOException {
        StringBuilder poly = new StringBuilder();
        for(int i = 0; i < powers.length; i++){
            poly.append(coefficients[i]).append("x").append(powers[i]);
            if(i+1 != powers.length && coefficients[i+1] > 0 ){
                poly.append("+");
            }
        }
        BufferedWriter output = new  BufferedWriter(new FileWriter(fileName));
        output.write(poly.toString());
        output.close();

    }
    public Polynomial add(Polynomial polynomial2){
        Polynomial newPolynomial = new Polynomial();
        int lengthOfArrays = powers.length;
        for (int i = 0; i < polynomial2.powers.length; i++){
            for (int k = 0; k < powers.length; k++){
                if(powers[k] == polynomial2.powers[i]){
                    break;
                }
                else if(k+1 == powers.length){
                    lengthOfArrays++;
                }
            }
        }

        newPolynomial.coefficients = new double[lengthOfArrays];
        newPolynomial.powers = new int[lengthOfArrays];

        int first = 0, second = 0, end = 0;
        for (int i = 0; first < powers.length && second < polynomial2.powers.length; i++, end++){

            if(powers[first] < polynomial2.powers[second]){
                newPolynomial.powers[i] = powers[first];
                first++;
            }
            else if (polynomial2.powers[second] < powers[first]) {
                newPolynomial.powers[i] = polynomial2.powers[second];
                second++;
            }
            else{
                newPolynomial.powers[i] = polynomial2.powers[second];
                first++;
                second++;
            }
        }

        if(first == powers.length){
            while(end < lengthOfArrays){
                newPolynomial.powers[end] = polynomial2.powers[second];
                end++;
                second++;
            }
        }
        else if(second == polynomial2.powers.length){
            while(end < lengthOfArrays){
                newPolynomial.powers[end] = powers[first];
                end++;
                first++;
            }
        }

        for(int old = 0, newest = 0; old < powers.length; newest++){
            if(powers[old] == newPolynomial.powers[newest]) {
                newPolynomial.coefficients[newest] += coefficients[old];
                old++;
            }
        }
        for(int old = 0, newest = 0; old < polynomial2.powers.length; newest++){
            if(polynomial2.powers[old] == newPolynomial.powers[newest]) {
                newPolynomial.coefficients[newest] += polynomial2.coefficients[old];
                old++;
            }
        }
        int newPolynomiallength = 0;
        for (int i = 0; i <newPolynomial.powers.length;i++){
            if (newPolynomial.coefficients[i] != 0){
                newPolynomiallength++;
            }
        }
        Polynomial newPolynomial2 = new Polynomial();
        newPolynomial2.coefficients = new double[newPolynomiallength];
        newPolynomial2.powers = new int[newPolynomiallength];

        for (int i = 0, traverse = 0; i < newPolynomial.powers.length; i++){
            if(newPolynomial.coefficients[i] != 0){
                newPolynomial2.coefficients[traverse] = newPolynomial.coefficients[i];
                newPolynomial2.powers[traverse] = newPolynomial.powers[i];
                traverse++;
            }
        }
        return newPolynomial2;
    }
    public double evaluate(double x){
        double total = 0;
        for (int i = 0; i < coefficients.length; i++){
            total += coefficients[i]*Math.pow(x,powers[i]);
        }
        return total;
    }

    public boolean hasRoot(double x){
        return this.evaluate(x) == 0;
    }
    public Polynomial multiply(Polynomial poly2){
        if(poly2.powers.length == 0 || powers.length == 0){
            return new Polynomial();
        }
        int pollen = (powers.length)*(poly2.powers.length);
        Polynomial newPoly = new Polynomial();
        newPoly.powers = new int[pollen];
        newPoly.coefficients = new double[pollen];
        int traverse = 0;
        for(int i = 0; i < powers.length; i++){
            for(int j = 0; j < poly2.powers.length; j++, traverse++){
                newPoly.powers[traverse] = powers[i] + poly2.powers[j];
            }
        }

        int temp = 0;

        for (int i = 0; i < pollen - 1; i++){
            for(int j = 0; j < pollen - 1; j++){
                if(newPoly.powers[j] > newPoly.powers[j + 1]){
                    temp = newPoly.powers[j];
                    newPoly.powers[j] = newPoly.powers[j + 1];
                    newPoly.powers[j + 1] = temp;
                }
            }
        }

        temp = 0;

        for(int i = 0; i < newPoly.powers.length; i++){
            for (int j = i + 1; j < newPoly.powers.length; j++){
                if(newPoly.powers[i] == newPoly.powers[j]) {
                    temp++;
                }
            }
        }
        Polynomial newPoly2 = new Polynomial();
        newPoly2.powers = new int[pollen - temp];
        newPoly2.coefficients = new double[pollen - temp];

        traverse = 1;
        temp = newPoly.powers[0];
        newPoly2.powers[0] = newPoly.powers[0];
        for (int i = 1; i < newPoly.powers.length; i++){
            if(newPoly.powers[i] != temp){
                newPoly2.powers[traverse] = newPoly.powers[i];
                temp = newPoly.powers[i];
                traverse++;
            }
        }

        int tempPower = 0;
        double tempCoefficient = 0.0;
        for(int first = 0; first < powers.length; first++){
            for(int second = 0; second < poly2.powers.length; second++){
                tempPower = powers[first] + poly2.powers[second];
                tempCoefficient = coefficients[first] * poly2.coefficients[second];
                for(int third = 0; third < newPoly2.powers.length; third++){
                    if(tempPower == newPoly2.powers[third]){
                        newPoly2.coefficients[third] += tempCoefficient;
                    }
                }
            }
        }

    return newPoly2;

    }

}
