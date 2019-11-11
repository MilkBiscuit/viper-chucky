/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.chuckyfacts.view.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.raywenderlich.chuckyfacts.BaseApplication
import com.raywenderlich.chuckyfacts.SplashContract
import com.raywenderlich.chuckyfacts.presenter.SplashPresenter
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward

class SplashActivity : AppCompatActivity(), SplashContract.View {

  private val navigator: Navigator? by lazy {
    object : Navigator {
      override fun applyCommand(command: Command) {
        if (command is Forward) {
          forward(command)
        }
      }

      private fun forward(command: Forward) {
        when (command.screenKey) {
          MainActivity.TAG -> startActivity(Intent(this@SplashActivity, MainActivity::class.java))
          else -> Log.e("Cicerone", "Unknown screen: " + command.screenKey)
        }
      }
    }
  }
  private var presenter: SplashContract.Presenter? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    presenter = SplashPresenter(this)
  }

  override fun onResume() {
    super.onResume()
    BaseApplication.INSTANCE.cicerone.navigatorHolder.setNavigator(navigator)
    presenter?.onViewCreated()
  }

  override fun onPause() {
    super.onPause()
    BaseApplication.INSTANCE.cicerone.navigatorHolder.removeNavigator()
  }

  override fun finishView() {
    // close splash activity
    finish()
  }

  override fun onDestroy() {
    presenter?.onDestroy()
    presenter = null
    super.onDestroy()
  }
}
