/**
 * #%L
 * This file is part of "Apromore Enterprise Edition".
 * %%
 * Copyright (C) 2019 - 2021 Apromore Pty Ltd. All Rights Reserved.
 * %%
 * NOTICE:  All information contained herein is, and remains the
 * property of Apromore Pty Ltd and its suppliers, if any.
 * The intellectual and technical concepts contained herein are
 * proprietary to Apromore Pty Ltd and its suppliers and may
 * be covered by U.S. and Foreign Patents, patents in process,
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this
 * material is strictly forbidden unless prior written permission
 * is obtained from Apromore Pty Ltd.
 * #L%
 */
package org.apromore.portal.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import javax.servlet.ServletContext;

public class LabelLocatorService implements org.zkoss.util.resource.LabelLocator {
  private ServletContext _svlctx;
  private String _name;

  public LabelLocatorService(ServletContext svlctx, String name) {
    _svlctx = svlctx;
    _name = name;
  }

  public URL locate(Locale locale) throws MalformedURLException {

    if (locale != null
        && getClass().getClassLoader().getResource(_name + "_" + locale + ".properties") != null) {
      return getClass().getClassLoader().getResource(_name + "_" + locale + ".properties");
    } else
      return getClass().getClassLoader().getResource(_name + ".properties");

  }
}
