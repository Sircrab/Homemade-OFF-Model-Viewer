package com.vidaric.main;

import com.joml.Vector3f;

public class MyVertex3f extends Vector3f {
	public MyVertex3f(float x, float y, float z){
		super(x,y,z);
	}
	
	public MyVertex3f(){
		super();
	}
	
	@Override
	public int hashCode() {
        int hash = 37;
        hash += 37 * hash + Float.floatToIntBits(x);
        hash += 37 * hash + Float.floatToIntBits(y);
        hash += 37 * hash + Float.floatToIntBits(z);
        return hash;
    }
	
	@Override
	public boolean equals(Object o) {
        if (!(o instanceof Vector3f) && !(o instanceof MyVertex3f)) { return false; }

        if (this == o) { return true; }

        Vector3f comp = (Vector3f) o;
        if (Float.compare(x,comp.x) != 0) return false;
        if (Float.compare(y,comp.y) != 0) return false;
        if (Float.compare(z,comp.z) != 0) return false;
        return true;
    }
}
