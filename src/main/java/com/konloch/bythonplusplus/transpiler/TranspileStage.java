package com.konloch.bythonplusplus.transpiler;

import com.konloch.bythonplusplus.BythonPlusPlus;

/**
 * @author Konloch
 * @since 7/2/2024
 */
public interface TranspileStage
{
	String fromPython(BythonPlusPlus bpp, String code);
	String fromBythonPP(BythonPlusPlus bpp, String code);
}
