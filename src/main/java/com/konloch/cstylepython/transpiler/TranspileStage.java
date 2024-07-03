package com.konloch.cstylepython.transpiler;

import com.konloch.cstylepython.CStylePython;

/**
 * @author Konloch
 * @since 7/2/2024
 */
public interface TranspileStage
{
	String fromPython(CStylePython cpy, String code);
	String fromBythonPP(CStylePython cpy, String code);
}
