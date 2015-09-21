package pa1;

public class Matrix {
	
	public float[][] matrix;
	public int size_row;
	public int size_col;
	public boolean square;
	
	public Matrix(int size, float[][] matrix) {
		this.square = true;
		this.matrix = matrix;
		this.size_row = size;
		this.size_col = size;
	}
	
	public Matrix(float[][] matrix) {
		this.size_row = matrix.length;
		this.size_col = matrix[0].length;
		this.matrix = matrix;
		if(this.size_row == this.size_col) {
			this.square = true;
		} else {
			this.square = false;
		}
	}
	
	public Matrix(Matrix m) {
		this.matrix = m.matrix;
		this.size_col = m.size_col;
		this.size_row = m.size_row;
		this.square = m.square;
	}
	
	public String toString() {
		String result = "";
		for(int i = 0; i < this.size_row; i++) {
			for(int j = 0; j < this.size_col; j++) {
				result += this.matrix[i][j] + "\t";
			}
			result += "\n";
		}
		return result;
		
	}
	
	public static Matrix get_transpose(Matrix m) {
		float[][] mT = new float[m.size_col][m.size_row];
		for(int c = 0; c < m.size_col; c++) {
			for(int r = 0; r < m.size_row; r++) {
				mT[c][r] = m.matrix[r][c];
			}
		}
		return new Matrix(mT);
	}
	
	public boolean equals(Matrix b) {
		if(this.size_row == b.size_row && this.size_col == b.size_col) {
			for(int r = 0; r < this.size_row; r++) {
				for(int c = 0; c < this.size_col; c++) {
					if(this.matrix[r][c] != b.matrix[r][c]) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
	
	public static Matrix get_identity(int size) {
		float[][] i = new float[size][size];
		for(int r = 0; r < size; r++) {
			for(int c = 0; c < size; c++) {
				if(r == c) {
					i[r][c] = 1f;
				} else {
					i[r][c] = 0f;
				}
			}
		}
		return new Matrix(i);
	}
	
	public static Matrix matrix_multiplication(Matrix lhs, Matrix rhs) {
		if(lhs.size_col != rhs.size_row) {
			System.out.println("Invalid sizes! Can not multiply!");
			return null;
		} else {
			float[][] product = new float[lhs.size_row][rhs.size_col];
			for(int lr = 0; lr < lhs.size_row; lr++) {
				for(int rc = 0; rc < rhs.size_col; rc++) {
					float sum = 0;
					for(int lc = 0; lc < lhs.size_col; lc++) {
						sum += (lhs.matrix[lr][lc] * rhs.matrix[lc][rc]);
					}
					product[lr][rc] = sum;
				}
			}
			
			return new Matrix(product);
		}
		
	}
	
	public boolean is_orthonormal() {
		Matrix t = get_transpose(this);
		Matrix mt = matrix_multiplication(this, t);
		if(mt.equals(get_identity(this.size_col))) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		float a[][] = {{1.00f, 2.0f}, {3.0f, 4.0f}, {5.0f, 6.0f}, {7f, 8f}, {9f, 10f}};
		float b[][] = {{1.0f, 2.0f, 3.0f, 7f}, {4.0f, 5.0f, 6.0f, 8f}};
		Matrix aa = new Matrix(a);
		Matrix bb = new Matrix(b);
		Matrix cc = matrix_multiplication(aa, bb);
		System.out.println(cc);
		Matrix dd = get_transpose(cc);
		System.out.println(dd);
		Matrix dddd = matrix_multiplication(dd, get_identity(5));
		if(dd.equals(dddd)) {
			System.out.println("EQUAL!");
		}
	}

}
