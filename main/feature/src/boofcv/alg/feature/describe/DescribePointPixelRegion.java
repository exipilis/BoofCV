/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://www.boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcv.alg.feature.describe;

import boofcv.struct.feature.TupleDesc;
import boofcv.struct.image.ImageBase;

/**
 * Describes a rectangular region using its raw pixel intensities. Score between two regions of this type is typically
 * computed using Sum of Absolute Differences (SAD).
 *
 * @author Peter Abeles
 */
public abstract class DescribePointPixelRegion<T extends ImageBase, D extends TupleDesc> {

	// image that descriptors are being extracted from
	protected T image;

	// size of the extracted region
	protected int regionWidth;
	protected int regionHeight;
	// radius from focal pixel
	protected int radiusWidth;
	protected int radiusHeight;

	// offset in terms of pixel index from the center pixel
	protected int offset[];

	public DescribePointPixelRegion(int regionWidth, int regionHeight) {
		this.regionWidth = regionWidth;
		this.regionHeight = regionHeight;

		this.radiusWidth = regionWidth/2;
		this.radiusHeight = regionHeight/2;

		offset = new int[ regionHeight*regionWidth ];
	}

	public void setImage( T image ) {
		this.image = image;

		for( int i = 0; i < regionHeight; i++ ) {
			for( int j = 0; j < regionWidth; j++ ) {
				offset[i*regionWidth+j] = (i-radiusHeight)*image.stride + j-radiusWidth;
			}
		}
	}

	public abstract void process( int c_x , int c_y , D desc );

	public int getDescriptorLength() {
		return offset.length;
	}

	public int getDescriptorRadius() {
		return Math.max(radiusHeight,radiusWidth);
	}

	public int getRegionWidth() {
		return regionWidth;
	}

	public int getRegionHeight() {
		return regionHeight;
	}
}
