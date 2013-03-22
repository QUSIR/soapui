/*
 *  soapUI, copyright (C) 2004-2011 smartbear.com 
 *
 *  soapUI is free software; you can redistribute it and/or modify it under the 
 *  terms of version 2.1 of the GNU Lesser General Public License as published by 
 *  the Free Software Foundation.
 *
 *  soapUI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU Lesser General Public License for more details at gnu.org.
 */

package com.eviware.x.impl.swt;

import javax.swing.ImageIcon;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.types.StringToStringMap;
import com.eviware.x.form.ValidationMessage;
import com.eviware.x.form.XForm;
import com.eviware.x.form.XFormField;
import com.eviware.x.impl.swing.AbstractSwingXFormField;

/**
 * @author Lars H
 */
public class SwtFormDialog extends AbstractDialog
{
   // On SWT (on Macintosh), parent components must be created before children.
   // The interface of this class is designed for Swing, where this was not a requirement.
   // Instead of changing that interface, this class first operates in a record mode
   // where all forms are mocked and method calls recorded. In this mode, form == null.
   // Then, the components are created and the recorded calls repeated on real components.
   private InvocationRecorder<XForm> formHandler;
   private SwtXFormImpl form;

   // Cannot use SWT controls after dispose, so store values here.
   private StringToStringMap storedResult;
   
   private final String title;
   private final String message;
   private final ImageIcon image;

   public SwtFormDialog(String title, String message, ImageIcon image)
   {
      super(title);
      this.title = title;
      this.message = message;
      this.image = image;

      // Mock a form.
      formHandler = new InvocationRecorder<XForm>(XForm.class);
   }
   
   @Override
   protected Control createDialogArea(Composite parent)
   {
      parent = (Composite) super.createDialogArea(parent);

      form = new SwtXFormImpl(parent, title);

      formHandler.playback(form);
      formHandler = null;
      
      return parent;
   }

   public XForm getForm()
   {
      if(form != null)
         return form;
      else if(formHandler != null)
         return formHandler.getMock();
      else
         throw new IllegalStateException("form is not set");
   }

   @Override
   public int open()
   {
      create();

      setTitle(title);
      setMessage(message);

      Image swtImage = SwtXFormDialogBuilder.getSwtImage(image);
      if (swtImage != null)
         setTitleImage(swtImage);

      storedResult = null;

      super.initializeBounds();
      return super.open();
   }

   @Override
   public boolean close()
   {
      // Store the values, because we cannot get them from the widgets after disposal.
      storedResult = new StringToStringMap();
      storeResult(storedResult);

      return super.close();
   }

   private void storeResult(StringToStringMap result)
   {
      result.putAll(form.getValues());
   }

   public StringToStringMap getValues()
   {
      if (storedResult != null)
      {
         return storedResult;
      }

      StringToStringMap result = new StringToStringMap();
      storeResult(result);
      return result;
   }

   // TODO Validate forms.
   public boolean validate()
   {
      XFormField[] formFields = form.getFormFields();
      for( int c = 0; c < formFields.length; c++ )
      {
         ValidationMessage [] messages = formFields[c].validate();
         if( messages != null && messages.length > 0 )
         {
//               tabs.setSelectedIndex( i );
            ((AbstractSwingXFormField)messages[0].getFormField()).getComponent().requestFocus();
            UISupport.showErrorMessage( messages[0].getMessage() );
            return false;
         }
      }
      
      return true;
   }
}