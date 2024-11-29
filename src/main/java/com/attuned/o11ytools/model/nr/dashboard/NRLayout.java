package com.attuned.o11ytools.model.nr.dashboard;

public class NRLayout {

    private int column;
    private int row;
    private int width;
    private int height;
    
    public NRLayout() {
    	
    }
    
	public NRLayout(int column, int row, int width, int height) {
		super();
		this.column = column;
		this.row = row;
		this.width = width;
		this.height = height;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return "NRLayout [column=" + column + ", row=" + row + ", width=" + width + ", height=" + height + "]";
	}
	
	
    
}
