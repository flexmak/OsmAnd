package net.osmand.plus.views.mapwidgets.utils;

import static java.lang.String.format;

import android.content.Context;

import androidx.annotation.NonNull;

import net.osmand.data.LatLon;
import net.osmand.plus.R;
import net.osmand.util.MapUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class GlideUtils {

	private static final NumberFormat FORMATTER = new DecimalFormat("#.#", new DecimalFormatSymbols(Locale.US));

	public static final float MAX_VALUE_TO_DISPLAY = 150.f;
	public static final float MAX_VALUE_TO_FORMAT = 100.f;
	public static final float MIN_ACCEPTABLE_VALUE = 0.1f;

	@NonNull
	public static String calculateFormattedRatio(@NonNull Context ctx, LatLon l1, LatLon l2, double altTo, double altFrom) {
		double distance = MapUtils.getDistance(l1, l2);
		return calculateFormattedRatio(ctx, distance, altTo - altFrom);
	}

	@NonNull
	public static String calculateFormattedRatio(@NonNull Context ctx, double distance, double altDif) {
		int sign = altDif < 0 ? -1 : 1;

		// Round arguments to '0' if they are smaller
		// in absolute value than the minimum acceptable value
		if (Math.abs(distance) < MIN_ACCEPTABLE_VALUE) {
			distance = 0;
		}
		if (Math.abs(altDif) < MIN_ACCEPTABLE_VALUE) {
			altDif = 0;
		}

		// Calculate and round glide ratio if needed
		float absRatio = 0;
		if (distance > 0) {
			absRatio = altDif != 0 ? (float) Math.abs(distance / altDif) : 1;
		}
		if (absRatio < MIN_ACCEPTABLE_VALUE) {
			absRatio = 0;
		}
		int divider = altDif != 0 ? 1 : 0;

		String pattern = ctx.getString(R.string.ltr_or_rtl_combine_via_colon_with_space);
		if (absRatio > MAX_VALUE_TO_DISPLAY || (absRatio == 1 && divider == 0)) {
			return format(pattern, "1", "0");
		} else if (absRatio > MAX_VALUE_TO_FORMAT) {
			return format(pattern, "" + (int) absRatio * sign, "" + divider);
		} else {
			return format(pattern, FORMATTER.format(absRatio * sign), "" + divider);
		}
	}

}
