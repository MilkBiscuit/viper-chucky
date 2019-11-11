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
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package com.raywenderlich.chuckyfacts.presenter

import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raywenderlich.chuckyfacts.BaseApplication
import com.raywenderlich.chuckyfacts.MainContract
import com.raywenderlich.chuckyfacts.entity.Joke
import com.raywenderlich.chuckyfacts.interactor.MainInteractor
import com.raywenderlich.chuckyfacts.view.activities.DetailActivity
import ru.terrakok.cicerone.Router


class MainPresenter(private var view: MainContract.View?) : MainContract.Presenter, MainContract.InteractorOutput {

  private var interactor: MainContract.Interactor? = MainInteractor()
  private val router: Router? by lazy { BaseApplication.INSTANCE.cicerone.router }

  override fun listItemClicked(joke: Joke?) {
    router?.navigateTo(DetailActivity.TAG, joke)
  }

  override fun onViewCreated() {
    view?.showLoading()
    interactor?.loadJokesList { result ->
      when (result) {
        is Result.Failure -> {
          this.onQueryError()
        }
        is Result.Success -> {
          val jokesJsonObject = result.get().obj()

          val type = object : TypeToken<List<Joke>>() {}.type
          val jokesList: List<Joke> =
              Gson().fromJson(jokesJsonObject.getJSONArray("value").toString(), type)

          this.onQuerySuccess(jokesList)
        }
      }
    }
  }

  override fun onQuerySuccess(data: List<Joke>) {
    view?.hideLoading()
    view?.publishDataList(data)
  }

  override fun onQueryError() {
    view?.hideLoading()
    view?.showInfoMessage("Error when loading data")
  }

  override fun onDestroy() {
    view = null
    interactor = null
  }
}
