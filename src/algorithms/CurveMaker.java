package algorithms;

public class CurveMaker {
    // Fungsi ngitung titik X dan Y ngelewatin 4 titik kontrol (P0, P1, P2, P3)
    public static int[] getBezierPoint(float t, int[] p0, int[] p1, int[] p2, int[] p3) {
        float u = 1 - t;
        float tt = t * t;
        float uu = u * u;
        float uuu = uu * u;
        float ttt = tt * t;

        // Implementasi rumus buat sumbu X dan Y
        int x = (int)(uuu * p0[0] + 3 * uu * t * p1[0] + 3 * u * tt * p2[0] + ttt * p3[0]);
        int y = (int)(uuu * p0[1] + 3 * uu * t * p1[1] + 3 * u * tt * p2[1] + ttt * p3[1]);
        
        return new int[]{x, y};
    }
}
