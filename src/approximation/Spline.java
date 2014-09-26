package approximation;

public class Spline extends BaseApproximation {

	public Spline(double[] xArray, double[] yArray) {
		super(xArray, yArray);
	}

	public class SplineTuple {
		public double a, b, c, d, x;
	}

	public SplineTuple[] splines; // ������

	
    public void approximazeFunction(int n) {
		// ������������� ������� ��������
		splines = new SplineTuple[n];
		for (int i = 0; i < n; i++) {
			splines[i] = new SplineTuple();
		}

		for (int i = 0; i < n; ++i) {
			splines[i].x = arrayX[i];
			splines[i].a = arrayY[i];
		}
		splines[0].c = splines[n - 1].c = 0.0;

		// ������� ���� ������������ ������������� �������� c[i] �������
		// �������� ��� ���������������� ������
		// ���������� ����������� ������������� - ������ ��� ������ ��������
		double[] alpha = new double[n - 1];
		double[] beta = new double[n - 1];
		alpha[0] = beta[0] = 0.0;
		for (int i = 1; i < n - 1; ++i) {
			double h_i = arrayX[i] - arrayX[i - 1], h_i1 = arrayX[i + 1] - arrayX[i];
			double A = h_i;
			double C = 2.0 * (h_i + h_i1);
			double B = h_i1;
			double F = 6.0 * ((arrayY[i + 1] - arrayY[i]) / h_i1 - (arrayY[i] - arrayY[i - 1]) / h_i);
			double z = (A * alpha[i - 1] + C);
			alpha[i] = -B / z;
			beta[i] = (F - A * beta[i - 1]) / z;
		}

		// ���������� ������� - �������� ��� ������ ��������
		for (int i = n - 2; i > 0; --i)
			splines[i].c = alpha[i] * splines[i + 1].c + beta[i];

		// ������������ ������, ���������� ������������ ��������������
		beta = null;
		alpha = null;

		// �� ��������� ������������� c[i] ������� �������� b[i] � d[i]
		for (int i = n - 1; i > 0; --i) {
			double h_i = arrayX[i] - arrayX[i - 1];
			splines[i].d = (splines[i].c - splines[i - 1].c) / h_i;
			splines[i].b = h_i * (2.0 * splines[i].c + splines[i - 1].c) / 6.0 + (arrayY[i] - arrayY[i - 1]) / h_i;
		}
	}

	// ���������� �������� ����������������� ������� � ������������ �����
	public double f(double x) {

		SplineTuple s;
		int n = splines.length;
		// BuildSpline(myx,y,n);
		if (x <= splines[0].x) // ���� x ������ ����� ����� x[0] - ����������
								// ������ ��-��� �������
			s = splines[1];
		else if (x >= splines[n - 1].x) // ���� x ������ ����� ����� x[n - 1] -
										// ���������� ��������� ��-��� �������
			s = splines[n - 1];
		else // ����� x ����� ����� ���������� ������� ����� - ����������
				// �������� ����� ������� ��-�� �������
		{
			int i = 0, j = n - 1;
			while (i + 1 < j) {
				int k = i + (j - i) / 2;
				if (x <= splines[k].x)
					j = k;
				else
					i = k;
			}
			s = splines[j];
		}

		double dx = (x - s.x);
		// ��������� �������� ������� � �������� ����� �� ����� ������� (�
		// ��������, "�����" ���������� �������� �� ����� ������� ���, �� ����
		// �� ��� ��� ����, ��� �������)
		return s.a + (s.b + (s.c / 2.0 + s.d * dx / 6.0) * dx) * dx;
	}

	@Override
	public double approximazeFunction(double point) {
		// TODO Auto-generated method stub
		return 0;
	}

}
