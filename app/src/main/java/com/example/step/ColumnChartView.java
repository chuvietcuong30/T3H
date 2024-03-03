package com.example.step;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ColumnChartView extends View {
    private List<Integer> columnData;
    private List<Integer> columnColors;
    private Paint columnPaint;
    private Paint textPaint;
    private int columnWidth;
    private int columnSpacing;

    public ColumnChartView(Context context) {
        super(context);
        init();
    }

    public ColumnChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColumnChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        columnData = new ArrayList<>();
        columnColors = new ArrayList<>(); // Khởi tạo mảng màu
        columnPaint = new Paint();
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(24);
        columnWidth = 135;
        columnSpacing = 20;
    }

    public void setColumnData(List<Integer> data) {
        this.columnData = data;
        generateRandomColors(); // Tạo màu ngẫu nhiên cho mỗi cột
        invalidate();
    }

    public void addColumn(int height) {
        columnData.add(height);
        if (columnData.size() > 7) {
            // Remove the first column if more than 7 columns
            columnData.remove(0);
            columnColors.remove(0); // Xóa màu của cột đầu tiên
        }
        generateRandomColors(); // Tạo màu ngẫu nhiên cho cột mới
        invalidate();
    }

    private void generateRandomColors() {
        columnColors.clear(); // Xóa mảng màu hiện tại
        for (int i = 0; i < columnData.size(); i++) {
            // Tạo màu ngẫu nhiên và thêm vào mảng
            int color = getRandomColor();
            columnColors.add(color);
        }
    }

    private int getRandomColor() {
        return Color.rgb((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int startX = 0;

        for (int i = 0; i < columnData.size(); i++) {
            int height = columnData.get(i);
            int columnHeight = height;

            columnPaint.setColor(columnColors.get(i)); // Lấy màu từ mảng màu

            canvas.drawRect(startX, getHeight() - columnHeight, startX + columnWidth, getHeight(), columnPaint);

            String text = String.valueOf(height);
            float textX = startX + columnWidth / 2 - textPaint.measureText(text) / 2;
            float textY = getHeight() - columnHeight - textPaint.descent();
            canvas.drawText(text, textX, textY, textPaint);

            startX += columnWidth + columnSpacing;
        }
    }
}