/*
 * Copyright (c) 2011-2017, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
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

package boofcv.demonstrations.shapes;

import boofcv.gui.BoofSwingUtil;
import boofcv.gui.StandardAlgConfigPanel;

import javax.swing.*;

import static boofcv.gui.BoofSwingUtil.MAX_ZOOM;
import static boofcv.gui.BoofSwingUtil.MIN_ZOOM;

public abstract class DetectBlackShapePanel extends StandardAlgConfigPanel {

	protected JSpinner selectZoom;
	protected JLabel processingTimeLabel = new JLabel();

	protected int selectedView = 0;

	protected double zoom = 1;

	public void setZoom( double _zoom ) {
		_zoom = Math.max(MIN_ZOOM,_zoom);
		_zoom = Math.min(MAX_ZOOM,_zoom);
		if( _zoom == zoom )
			return;
		zoom = _zoom;

		BoofSwingUtil.invokeNowOrLater(new Runnable() {
			@Override
			public void run() {
				selectZoom.setValue(zoom);
			}
		});
	}

	public void setProcessingTime( double seconds ) {
		processingTimeLabel.setText(String.format("%7.1f",(seconds*1000)));
	}
}
